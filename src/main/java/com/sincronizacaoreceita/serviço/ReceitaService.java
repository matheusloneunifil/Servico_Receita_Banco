package com.sincronizacaoreceita.serviço;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Autor: Matheus Lone
 */
@Service
public class ReceitaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceitaService.class);

    @Value("${separador-arquivo:;}")
    public String splitBy;
    List<String> tipos = new ArrayList();

    @PostConstruct
    public void init()
    {
        // Types of valid statuses:
        tipos.add("A");
        tipos.add("I");
        tipos.add("B");
        tipos.add("P");
    }

    public boolean atualizarConta(String agencia, String conta, double saldo, String status)
            throws RuntimeException, InterruptedException {


        if (agencia == null || agencia.length() != 4) {
            return false;
        }
        

        if (conta == null || conta.length() != 6) {
            return false;
        }
        

        if (status == null || !tipos.contains(status)) {
            return false;
        }


        long wait = Math.round(Math.random() * 4000) + 1000;
        Thread.sleep(wait);


        long randomError = Math.round(Math.random() * 1000);
        if (randomError == 500) {
            throw new RuntimeException("Error");
        }

        return true;
    }

    public ByteArrayInputStream download() {
        final CSVFormat format = CSVFormat.newFormat(splitBy.charAt(0)).withEscape('\\').withRecordSeparator("\n");
        List<String> data = Arrays.asList(
                "fsdf",
                "sdfsdf",
                "asfdasdfasfdasd",
                "sssssssssss"
        );
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream();
             final CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), format)) {
            adicionarHeaders(printer);
            printer.printRecord(data);
            printer.println();
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("falha ao importar dados ao arquivo CSV " + e.getMessage());
        }
    }
    public ByteArrayInputStream carregar(MultipartFile file) throws IOException {
        return lerCsv(file.getInputStream());
    }


    private ByteArrayInputStream lerCsv(InputStream is) throws IOException {
        LOGGER.info("lendo CSV");
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        final CSVFormat formato = CSVFormat.newFormat(splitBy.charAt(0)).withEscape('\\').withRecordSeparator("\n");
        CSVParser parserCsv = new CSVParser(fileReader, formato);
        Iterable<CSVRecord> memoriaCsv = parserCsv.getRecords();
        LOGGER.info("Atualizando novo CSV");
        return escreverCsv(memoriaCsv, formato);
    }

    private ByteArrayInputStream escreverCsv(Iterable<CSVRecord> csvRecords, CSVFormat format) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             CSVPrinter printerCsv = new CSVPrinter(new PrintWriter(output), format)) {
            /**
             * Adicionando Header Csv
             */
            adicionarHeaders(printerCsv);

            int i = 0;
            for (CSVRecord csvRecord : csvRecords) {
                /**
                 * ignorando headers
                 */
                if (i == 0) {
                    ++i;
                    continue;
                }

                /**
                 * status aleatorios pra tipos
                 */
                Random r = new Random();
                int itemAleatorio = r.nextInt(tipos.size());
                String elementoAleatorio = tipos.get(itemAleatorio);

                /**
                 * Valida a coluna
                 */
                String agencia = csvRecord.get(0).replaceAll("[^a-zA-Z0-9]", "").substring(0, 4);
                String conta = csvRecord.get(1).replaceAll("[^a-zA-Z0-9]", "").substring(0, 6);
                double saldo =  Double.valueOf(csvRecord.get(2).replace(",", "."));

                if(atualizarConta(agencia,conta,saldo,elementoAleatorio))
                {
                    List<String> dados = Arrays.asList(
                            agencia,
                            conta,
                            String.valueOf(saldo),
                            elementoAleatorio
                    );
                    printerCsv.printRecord(dados);
                }
            }
            printerCsv.flush();
            return new ByteArrayInputStream(output.toByteArray());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("falha ao importar dados ao arquivo CSV: " + e.getMessage());
        }
    }

    private void adicionarHeaders(CSVPrinter csvPrinter) throws IOException {
        csvPrinter.printRecord(Arrays.asList(
                "agencia",
                "conta",
                "saldo",
                "status"
        ));
    }
}
