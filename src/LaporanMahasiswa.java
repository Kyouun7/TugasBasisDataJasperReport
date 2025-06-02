import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

public class LaporanMahasiswa {
    public static void main(String[] args) {
        System.out.println("Classpath aktif: " + System.getProperty("java.class.path"));
        String url = "jdbc:sqlserver://localhost:1433;databaseName=University;integratedSecurity=true;encrypt=false";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url);

            System.out.println("Mencoba compile: students_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport("students_report.jrxml");
            System.out.println("Sukses compile jrxml");

            HashMap<String, Object> params = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, conn);

            String outputPath = "output_laporan_mahasiswa.pdf";
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputPath)));

            SimplePdfExporterConfiguration config = new SimplePdfExporterConfiguration();
            config.setMetadataAuthor("Laporan Mahasiswa");
            config.setEncrypted(false);
            exporter.setConfiguration(config);

            exporter.exportReport();
            System.out.println("📄 Laporan berhasil disimpan ke: " + outputPath);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
