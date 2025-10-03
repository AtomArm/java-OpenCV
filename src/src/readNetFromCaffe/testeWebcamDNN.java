package readNetFromCaffe;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

public class testeWebcamDNN {
    public static void main(String[] args) {
        // Carrega a biblioteca nativa do OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Caminhos para os arquivos do modelo
        String protoPath = "C:/Users/Pedro/Documents/GitHub/JavaOpenCV/src/src/readNetFromCaffe/resources/deploy.prototxt";
        String modelPath = "C:/Users/Pedro/Documents/GitHub/JavaOpenCV/src/src/readNetFromCaffe/resources/res10_300x300_ssd_iter_140000.caffemodel";

        // Carrega a rede DNN
        Net net = Dnn.readNetFromCaffe(protoPath, modelPath);
        if (net.empty()) {
            System.out.println("❌ Erro ao carregar a rede!");
            return;
        }

        // Abre a webcam
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("❌ Não foi possível abrir a webcam!");
            return;
        }

        Mat frame = new Mat();
        while (true) {
            if (!camera.read(frame) || frame.empty()) {
                System.out.println("❌ Não foi possível capturar o frame.");
                break;
            }

            // Cria o blob a partir da imagem
            Mat blob = Dnn.blobFromImage(
                    frame,
                    1.0,
                    new Size(300, 300),
                    new Scalar(104.0, 177.0, 123.0),
                    false,
                    false
            );

            // Faz o forward na rede
            net.setInput(blob);
            Mat detections = net.forward();

            // Percorre as detecções
            int cols = frame.cols();
            int rows = frame.rows();
            detections = detections.reshape(1, (int) detections.total() / 7);

            int faceCount = 0;
            for (int i = 0; i < detections.rows(); i++) {
                double confidence = detections.get(i, 2)[0];

                if (confidence > 0.5) { // confiança mínima
                    int x1 = (int) (detections.get(i, 3)[0] * cols);
                    int y1 = (int) (detections.get(i, 4)[0] * rows);
                    int x2 = (int) (detections.get(i, 5)[0] * cols);
                    int y2 = (int) (detections.get(i, 6)[0] * rows);

                    // Desenha retângulo
                    Imgproc.rectangle(frame,
                            new Point(x1, y1),
                            new Point(x2, y2),
                            new Scalar(0, 255, 0),
                            2
                    );

                    // Confiança em %
                    String label = String.format("Face: %.2f", confidence);
                    Imgproc.putText(frame, label,
                            new Point(x1, y1 - 10),
                            Imgproc.FONT_HERSHEY_SIMPLEX,
                            0.5,
                            new Scalar(0, 255, 0),
                            2
                    );

                    faceCount++;
                }
            }

            // Mostra contagem de rostos detectados
            Imgproc.putText(frame,
                    "Faces detectadas: " + faceCount,
                    new Point(10, 30),
                    Imgproc.FONT_HERSHEY_SIMPLEX,
                    0.8,
                    new Scalar(0, 255, 0),
                    2
            );

            // Exibe janela
            HighGui.imshow("Detecção de Faces (DNN) - Pressione ESC ou Q", frame);

            int key = HighGui.waitKey(1);
            if (key == 27 || key == 'q') { // ESC ou Q
                break;
            }
        }

        // Libera recursos
        camera.release();
        HighGui.destroyAllWindows();
    }
}
