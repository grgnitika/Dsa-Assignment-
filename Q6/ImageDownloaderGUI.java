/* 
Implement a Multithreaded Asynchronous Image Downloader in Java Swing
Task Description:
Design and implement a multithreaded asynchronous image downloader in a Java Swing 
application. The application should allow users to enter a URL and download images from 
that URL in the background, while keeping the UI responsive. The image downloader should 
utilize multithreading and provide a smooth user experience when downloading images.
*/

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ImageDownloaderGUI extends JFrame {
    private JTextField urlField;
    private JButton downloadButton, cancelButton, pauseResumeButton;
    private JPanel progressPanel;
    private JLabel statusLabel;

    private ExecutorService executorService;
    private List<DownloadTask> downloadTasks;
    private volatile boolean paused = false;

    public ImageDownloaderGUI() {
        setTitle("Image Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        urlField = new JTextField("https://img.freepik.com/premium-vector/flat-store-facade-with-awning_23-2147542588.jpg");
        downloadButton = new JButton("Download");
        cancelButton = new JButton("Cancel");
        pauseResumeButton = new JButton("Pause");

        inputPanel.add(new JLabel("URLs :"));
        inputPanel.add(urlField);
        inputPanel.add(downloadButton);
        inputPanel.add(cancelButton);
        inputPanel.add(pauseResumeButton);

        progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        statusLabel = new JLabel("Status: ");

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(progressPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);

        downloadButton.addActionListener(e -> startDownload());
        cancelButton.addActionListener(e -> cancelDownload());
        pauseResumeButton.addActionListener(e -> pauseResumeDownload());
        cancelButton.setEnabled(false);
        pauseResumeButton.setEnabled(false);
    }

    private void startDownload() {
        String urlsInput = urlField.getText().trim();
        String[] urls = urlsInput.split(",");
        if (urls.length > 0) {
            if (executorService == null || executorService.isShutdown()) {
                // Create a ThreadPoolExecutor with a fixed number of threads
                int numberOfThreads = Runtime.getRuntime().availableProcessors(); // Get the number of available processors
                executorService = Executors.newFixedThreadPool(numberOfThreads);
                downloadTasks = new ArrayList<>();
            }

            for (String url : urls) {
                final String currentUrl = url.trim();
                if (!currentUrl.isEmpty()) {
                    DownloadTask downloadTask = new DownloadTask(currentUrl);
                    downloadTasks.add(downloadTask);
                    executorService.submit(downloadTask);
                }
            }
            cancelButton.setEnabled(true);
            pauseResumeButton.setEnabled(true);
        }
    }

    private void cancelDownload() {
        if (downloadTasks != null) {
            for (DownloadTask task : downloadTasks) {
                task.cancel();
            }
            downloadTasks.clear();
        }
        statusLabel.setText("Status: Download Cancelled");
        cancelButton.setEnabled(false);
        pauseResumeButton.setEnabled(false);
    }

    private void pauseResumeDownload() {
        paused = !paused;
        pauseResumeButton.setText(paused ? "Resume" : "Pause");
        if (!paused) {
            for (DownloadTask task : downloadTasks) {
                task.resumeDownload();
            }
        } else {
            for (DownloadTask task : downloadTasks) {
                task.pauseDownload();
            }
        }
    }

    private class DownloadTask implements Runnable {
        private String url;
        private JProgressBar progressBar;
        private JLabel downloadStatusLabel;
        private volatile boolean cancelled = false;
        private volatile boolean pausedTask = false;

        public DownloadTask(String url) {
            this.url = url;
            progressBar = new JProgressBar();
            downloadStatusLabel = new JLabel("Downloading " + url + "...");
            progressPanel.add(downloadStatusLabel);
            progressPanel.add(progressBar);
            progressPanel.revalidate();
        }

        public void run() {
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                long totalBytes = connection.getContentLengthLong();
                InputStream inputStream = connection.getInputStream();
                String fileName = imageUrl.getFile();
                int index = fileName.lastIndexOf('/');
                if (index != -1 && index < fileName.length() - 1) {
                    fileName = fileName.substring(index + 1);
                } else {
                    fileName = "downloaded_image";
                }
                File file = new File(fileName);

                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    synchronized (this) {
                        while (pausedTask && !cancelled) {
                            wait();
                        }
                    }
                    if (cancelled) {
                        downloadStatusLabel.setText("Download Cancelled");
                        return;
                    }
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    int progress = (int) ((totalBytesRead * 100) / totalBytes);
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);
                        downloadStatusLabel.setText("Downloading " + url + "... " + progress + "%");
                    });
                    Thread.sleep(100);
                }

                inputStream.close();
                outputStream.close();

                SwingUtilities.invokeLater(() -> {
                    downloadStatusLabel.setText("Download Complete for " + url);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    downloadStatusLabel.setText("Error downloading image from " + url);
                });
            }
        }

        public synchronized void cancel() {
            cancelled = true;
        }

        public synchronized void pauseDownload() {
            pausedTask = true;
        }

        public synchronized void resumeDownload() {
            pausedTask = false;
            notify();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageDownloaderGUI::new);
    }
}