// Implement a Multithreaded Asynchronous Image Downloader in Java Swing
// Task Description:
// You are tasked with designing and implementing a multithreaded asynchronous image downloader 
// in a Java Swing application. The application should allow users to enter a URL and download 
// images from that URL in the background, while keeping the UI responsive. The image downloader 
// should utilize multithreading and provide a smooth user experience when downloading images.

// Requirements:
// Design and implement a GUI application that allows users to enter a URL and download images.
// Implement a multithreaded asynchronous framework to handle the image downloading process in the 
// background.
// Provide a user interface that displays the progress of each image download, including the current 
// download status and completion percentage.
// Utilize a thread pool to manage the concurrent downloading of multiple images, ensuring efficient 
// use of system resources.
// Implement a mechanism to handle downloading errors or exceptions, displaying appropriate error 
// messages to the user.
// Use thread synchronization mechanisms, such as locks or semaphores, to ensure data integrity and 
// avoid conflicts during image downloading.
// Provide options for the user to pause, resume, or cancel image downloads.
// Test the application with various URLs containing multiple images to verify its functionality and 
// responsiveness.
// Include proper error handling and reporting for cases such as invalid URLs or network failures.

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
    private JProgressBar progressBar;
    private JLabel statusLabel;

    private ExecutorService executorService;
    private List<Future<?>> downloadTasks;
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

        progressBar = new JProgressBar();
        statusLabel = new JLabel("Status: ");

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.CENTER);
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
                executorService = Executors.newCachedThreadPool();
                downloadTasks = new ArrayList<>();
            }

            for (String url : urls) {
                final String currentUrl = url.trim();
                if (!currentUrl.isEmpty()) {
                    Future<?> downloadTask = executorService.submit(() -> {
                        try {
                            @SuppressWarnings("deprecation")
                            URL imageUrl = new URL(currentUrl);
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
                                if (paused) {
                                    statusLabel.setText("Status: Download Paused");
                                    while (paused) {
                                        Thread.sleep(100);
                                    }
                                    statusLabel.setText("Status: Download Resumed");
                                }
                                outputStream.write(buffer, 0, bytesRead);
                                totalBytesRead += bytesRead;
                                int progress = (int) ((totalBytesRead * 100) / totalBytes);
                                SwingUtilities.invokeLater(() -> {
                                    progressBar.setValue(progress);
                                    statusLabel.setText("Status: Downloading " + currentUrl + "... " + progress + "%");
                                });
                                Thread.sleep(100);
                            }

                            inputStream.close();
                            outputStream.close();

                            SwingUtilities.invokeLater(() -> {
                                statusLabel.setText("Status: Download Complete for " + currentUrl);
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(() -> {
                                statusLabel.setText("Status: Error downloading image from " + currentUrl);
                            });
                        }
                    });
                    downloadTasks.add(downloadTask);
                }
            }
            cancelButton.setEnabled(true);
            pauseResumeButton.setEnabled(true);
        }
    }

    private void cancelDownload() {
        if (downloadTasks != null) {
            for (Future<?> task : downloadTasks) {
                if (!task.isDone() && !task.isCancelled()) {
                    task.cancel(true);
                }
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageDownloaderGUI::new);
    }
}
