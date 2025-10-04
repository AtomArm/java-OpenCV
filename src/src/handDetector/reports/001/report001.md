# Relatório de Uso do Algoritmo de Detecção

## Introdução

Este relatório apresenta a aplicação de um algoritmo de detecção de mãos desenvolvido em Java utilizando a biblioteca OpenCV, no contexto do projeto de Robótica.

## Objetivo

O objetivo principal foi identificar e rastrear mãos em tempo real a partir de imagens capturadas pela webcam, permitindo a interação com o braço robótico por meio de gestos.

## Metodologia

- Utilização do OpenCV para captura de vídeo, pré-processamento (espelhamento, suavização, conversão de cor e segmentação por tons de pele).
- Aplicação de operações morfológicas e remoção de ruídos para melhorar a segmentação.
- Detecção de contornos e filtragem por área para identificar a mão.
- Cálculo do contorno convexo (convex hull) e defeitos de convexidade para estimar o número de dedos visíveis.
- Classificação do gesto com base na contagem de dedos detectados.

## Resultados

- O algoritmo funcionou de forma estável em ambientes bem iluminados, detectando e classificando gestos simples (como punho fechado e contagem de dedos).
- Em cenários com fundo complexo ou iluminação inadequada, a precisão da detecção foi reduzida.
- O tempo de processamento foi suficiente para aplicações em tempo real, com resposta visual imediata na interface.

## Conclusão

A implementação do algoritmo de detecção de mãos demonstrou ser eficiente para o controle do braço robótico via gestos, embora apresente limitações em condições adversas de iluminação e fundo.

## Próximos Passos

- Aprimorar o pré-processamento das imagens para maior robustez em diferentes condições.
- Explorar outros classificadores e técnicas de aprendizado de máquina para melhorar a precisão.
- Integrar feedback visual mais detalhado para o usuário durante a operação.
