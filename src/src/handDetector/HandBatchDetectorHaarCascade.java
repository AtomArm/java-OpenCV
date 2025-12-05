import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

public class HandBatchDetectorHaarCascade {

    static String BASE_PATH = "src/src/HandDetector/benchmark/";
    static String XML_PATH = BASE_PATH + "resources/hand.xml";
    static String INPUT_DIR = BASE_PATH + "images/dataset";
    static String OUTPUT_CSV = BASE_PATH + "batch_results_haar.csv";
    static String OUTPUT_IMG_DIR = BASE_PATH + "images/processed_haar/";

    public static void main(String[] args) throws IOException {
        // 1. Carregar a biblioteca nativa do OpenCV (necessário se não estiver carregado externamente)
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 

        // 2. Carregar o Classificador
        CascadeClassifier handDetector = new CascadeClassifier(XML_PATH);
        if (handDetector.empty()) {
            System.out.println("❌ Erro: Não foi possível carregar o XML em: " + XML_PATH);
            return;
        }

        // Criar diretório de saída se não existir
        Files.createDirectories(Paths.get(OUTPUT_IMG_DIR));

        try (PrintWriter csvWriter = new PrintWriter(new FileWriter(OUTPUT_CSV))) {
            // Cabeçalho adaptado para Haar (foco em tempo e detecção)
            csvWriter.println("file,detected_count,time_ns,first_x,first_y,first_w,first_h");

            System.out.println("🚀 Iniciando Batch Processing com Haar Cascade...");

            try (Stream<Path> paths = Files.walk(Paths.get(INPUT_DIR))) {
                paths.filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().endsWith(".jpg") || p.toString().toLowerCase().endsWith(".png"))
                        .forEach(path -> {

                            Mat frame = Imgcodecs.imread(path.toString());
                            if (frame.empty()) {
                                System.out.println("❌ Erro ao ler imagem: " + path);
                                return;
                            }

                            // --- INÍCIO DA MEDIÇÃO DE TEMPO (CRÍTICO PARA O ARTIGO) ---
                            long startTime = System.nanoTime();

                            MatOfRect hands = new MatOfRect();

                            // PARÂMETROS PADRONIZADOS (Conforme Metodologia)
                            // scaleFactor=1.1, minNeighbors=1, minSize=(150, 150)
                            handDetector.detectMultiScale(
                                    frame,
                                    hands,
                                    1.1,
                                    1,
                                    0,
                                    new Size(150, 150),
                                    new Size()
                            );

                            long endTime = System.nanoTime();
                            long duration = endTime - startTime;
                            // --- FIM DA MEDIÇÃO ---

                            Rect[] handsArray = hands.toArray();
                            int count = handsArray.length;

                            // Desenhar retângulos e preparar dados para o CSV
                            int firstX = 0, firstY = 0, firstW = 0, firstH = 0;

                            for (int i = 0; i < count; i++) {
                                Rect r = handsArray[i];
                                Imgproc.rectangle(frame, r, new Scalar(0, 255, 0), 2);

                                // Pegar dados do primeiro para registro (caso haja múltiplos)
                                if (i == 0) {
                                    firstX = r.x; firstY = r.y; firstW = r.width; firstH = r.height;
                                }
                            }

                            // Escrever na imagem
                            showTextOnScreen(frame, count, duration, path.getFileName().toString());

                            // Salvar imagem processada
                            String outputImagePath = OUTPUT_IMG_DIR + path.getFileName().toString();
                            Imgcodecs.imwrite(outputImagePath, frame);

                            // Escrever no CSV
                            csvWriter.printf(Locale.US, "%s,%d,%d,%d,%d,%d,%d%n",
                                    path.getFileName().toString(),
                                    count,
                                    duration, // Tempo em nanosegundos (bom para precisão)
                                    firstX,
                                    firstY,
                                    firstW,
                                    firstH
                            );

                            System.out.println("✅ Processado: " + path.getFileName() + " | Mãos: " + count + " | Tempo: " + (duration/1_000_000.0) + "ms");
                        });
            }
        }
        System.out.println("🏁 Batch finalizado. Resultados em: " + OUTPUT_CSV);
    }

    public static void showTextOnScreen(Mat frame, int count, long timeNs, String filename) {
        double timeMs = timeNs / 1_000_000.0;
        Imgproc.putText(frame, "Hands: " + count, new Point(10, 30),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(0, 0, 255), 2);
        Imgproc.putText(frame, String.format("Time: %.2f ms", timeMs), new Point(10, 60),
                Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(0, 0, 255), 2);
    }
}