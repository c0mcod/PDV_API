package com.pdv.lalapan.services;

import com.pdv.lalapan.dto.historicoVendas.HistoricoVendasResponseDTO;
import com.pdv.lalapan.entities.Produto;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportarProdutos(List<Produto> produtos) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Produtos");

        CreationHelper createHelper = workbook.getCreationHelper();

        // Estilo contábil
        XSSFCellStyle contabilStyle = workbook.createCellStyle();
        contabilStyle.setDataFormat(createHelper.createDataFormat().getFormat("_-R$* #,##0.00_-"));

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        String[] colunas = {"CODIGO", "NOME", "CATEGORIA", "PREÇO DE CUSTO", "QUANTIDADE", "UNIDADE", "QTD. MIN", "VALOR", "STATUS"};
        for (int i = 0; i < colunas.length; i++) {
            headerRow.createCell(i).setCellValue(colunas[i]);
        }

        // Dados
        int rowNum = 1;
        for (Produto produto : produtos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(produto.getCodigo());
            row.createCell(1).setCellValue(produto.getNome());
            row.createCell(2).setCellValue(produto.getCategoria().toString());

            Cell precoCusto = row.createCell(3);
            precoCusto.setCellValue(produto.getPrecoCusto().doubleValue());
            precoCusto.setCellStyle(contabilStyle);

            row.createCell(4).setCellValue(produto.getQuantidadeEstoque().doubleValue());
            row.createCell(5).setCellValue(produto.getUnidade().toString());
            row.createCell(6).setCellValue(produto.getEstoqueMinimo().doubleValue());

            Cell valor = row.createCell(7);
            valor.setCellValue(produto.getPreco().doubleValue());
            valor.setCellStyle(contabilStyle);

            row.createCell(8).setCellValue(produto.getAtivo() ? "Ativo" : "Inativo");
        }

        // Tabela formatada
        int lastRow = rowNum - 1;
        AreaReference area = new AreaReference(new CellReference(0, 0), new CellReference(lastRow, 8), SpreadsheetVersion.EXCEL2007);
        XSSFTable tabela = sheet.createTable(area);
        tabela.setName("Produtos");
        tabela.setDisplayName("Produtos");
        tabela.getCTTable().addNewTableStyleInfo();
        tabela.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");
        tabela.getCTTable().getTableStyleInfo().setShowRowStripes(true);

        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    public byte[] exportarHistoricoVendas(List<HistoricoVendasResponseDTO> vendas, LocalDateTime dataInicio, LocalDateTime dataFim, Long operadorId, LocalDateTime criandoEm) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Historico de vendas");

        // Estilos
        CreationHelper createHelper = workbook.getCreationHelper();

        // Estilo cabeçalho (título, período, operador)
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null)); // branco
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte)0x1F, (byte)0x5C, (byte)0x7A}, null));
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Estilo data
        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm"));

        // Estilo contábil
        XSSFCellStyle contabilStyle = workbook.createCellStyle();
        contabilStyle.setDataFormat(createHelper.createDataFormat().getFormat("_-R$* #,##0.00_-"));

        // Linhas do cabeçalho
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Row titulo = sheet.createRow(0);
        Cell tituloCell = titulo.createCell(0);
        tituloCell.setCellValue("Relatório de Histórico de Vendas");
        tituloCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        Row periodo = sheet.createRow(1);
        Cell periodoCell = periodo.createCell(0);
        periodoCell.setCellValue("Período: " + dataInicio.format(formatter) + " até " + dataFim.format(formatter));
        periodoCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

        Row operadorRow = sheet.createRow(2);
        Cell operadorCell = operadorRow.createCell(0);
        operadorCell.setCellValue("Operador: " + (operadorId != null ? operadorId : "Todos"));
        operadorCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));

        Row criadoEmRow = sheet.createRow(3);
        Cell criadoEmCell = operadorRow.createCell(0);
        operadorCell.setCellValue("Criado em: " + criandoEm.format(formatter));
        operadorCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));

        sheet.createRow(4); // linha em branco

        // Cabeçalho da tabela
        Row headerRow = sheet.createRow(4);
        String[] colunas = {"ID VENDA", "DATA_ABERTURA", "DATA_FECHAMENTO", "OPERADOR", "VALOR_TOTAL", "TOTAL_ITENS"};
        for (int i = 0; i < colunas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(colunas[i]);
        }

        // Dados
        int rowNum = 5;
        for (HistoricoVendasResponseDTO historico : vendas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(historico.vendaId());

            Cell dataAbertura = row.createCell(1);
            dataAbertura.setCellValue(Date.from(historico.dataHoraAbertura().atZone(ZoneId.systemDefault()).toInstant()));
            dataAbertura.setCellStyle(dateStyle);

            Cell dataFechamento = row.createCell(2);
            dataFechamento.setCellValue(Date.from(historico.dataHoraFechamento().atZone(ZoneId.systemDefault()).toInstant()));
            dataFechamento.setCellStyle(dateStyle);

            row.createCell(3).setCellValue(historico.operadorNome());

            Cell valorTotal = row.createCell(4);
            valorTotal.setCellValue(historico.valorTotal().doubleValue());
            valorTotal.setCellStyle(contabilStyle);

            row.createCell(5).setCellValue(historico.totalItens());
        }

        // Tabela formatada
        int lastRow = rowNum - 1;
        AreaReference area = new AreaReference(new CellReference(4, 0), new CellReference(lastRow, 5), SpreadsheetVersion.EXCEL2007);
        XSSFTable tabela = sheet.createTable(area);
        tabela.setName("HistoricoVendas");
        tabela.setDisplayName("HistoricoVendas");
        tabela.getCTTable().addNewTableStyleInfo();
        tabela.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");
        tabela.getCTTable().getTableStyleInfo().setShowRowStripes(true);

        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

}
