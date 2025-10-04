import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;

public class testeWebcam {
    public static void main(String[] args) {
        // Carrega a biblioteca nativa do OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Inicia captura de vídeo da webcam
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("❌ Não foi possível abrir a webcam!");
            return;
        }

        // Caminho do classificador Haar
        String cascadePath = "resources/haarcascade_frontalface_default.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(cascadePath);

        if (faceDetector.empty()) {
            System.out.println("❌ Erro ao carregar o classificador: " + cascadePath);
            return;
        }

        Mat frame = new Mat();
        Mat grayFrame = new Mat();

        while (true) {
            if (!camera.read(frame) || frame.empty()) {
                System.out.println("❌ Não foi possível capturar o frame.");
                break;
            }

            // Converte para escala de cinza (mais rápido)
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(grayFrame, grayFrame); // normaliza iluminação

            // Detecta faces
            MatOfRect faces = new MatOfRect();
            faceDetector.detectMultiScale(
                    grayFrame,
                    faces,
                    1.1,   // fator de escala
                    5,     // vizinhos mínimos
                    0,
                    new Size(50, 50), // tamanho mínimo da face
                    new Size()
            );

            Rect[] facesArray = faces.toArray();

            // Desenha retângulos nas faces detectadas
            for (Rect rect : facesArray) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
            }

            // Mostra contagem no canto superior esquerdo
            Imgproc.putText(frame,
                    "Faces detectadas: " + facesArray.length,
                    new org.opencv.core.Point(10, 30),
                    Imgproc.FONT_HERSHEY_SIMPLEX,
                    0.8,
                    new Scalar(0, 255, 0),
                    2
            );

            // Exibe janela
            HighGui.imshow("Detecção de Faces - Pressione ESC ou Q para sair", frame);

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
