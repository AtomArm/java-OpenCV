# JavaOpenCV - Configuração do Projeto

Este projeto utiliza a biblioteca OpenCV para Java. Siga os passos abaixo para configurar corretamente o ambiente e executar o projeto.

## Pré-requisitos

- Java 21 instalado
- IntelliJ IDEA
- OpenCV 4.12.0 (arquivo `opencv-4120.jar` e `opencv_java4120.dll`)

## Estrutura esperada

## Configuração no IntelliJ IDEA

1. **Adicione o JAR do OpenCV ao projeto:**
    - Clique com o botão direito no projeto > `Open Module Settings` > `Libraries` > `+` > Adicione o arquivo `opencv-4120.jar` localizado em `modules/opencv-4120/`.

2. **Configure o caminho da biblioteca nativa:**
    - Vá em `Run > Edit Configurations...`
    - Selecione sua configuração de execução.
    - No campo **VM options**, adicione:
      ```
      -Djava.library.path=C:\Users\maria\OneDrive - FUNDAÇÃO MOVIMENTO DIREITO E CIDADANIA\Projetos\Robótica\Braço Robótico\Repos\JavaOpenCV\modules\opencv-4120
      ```
    - Salve e execute.

3. **Certifique-se de que o arquivo `opencv_java4120.dll` está na pasta acima.**

## Execução via terminal

Se preferir rodar pelo terminal, use:

## Observações

- Sempre ajuste o caminho do `-Djava.library.path` para onde está o arquivo `.dll`.
- Se ocorrer erro de biblioteca não encontrada, revise o caminho e a configuração da VM.

---
