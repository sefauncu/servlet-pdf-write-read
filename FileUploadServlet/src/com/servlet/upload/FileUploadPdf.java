package com.servlet.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
 
@WebServlet("/FileUpload")
@MultipartConfig
public class FileUploadPdf extends HttpServlet {
 
 
    private static final long serialVersionUID = 1L;
 
    protected void doPost(HttpServletRequest request,  HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
 
        final Part filePart = request.getPart("file");
        String bookId = request.getParameter("bookId");
 
        InputStream pdfFileBytes = null;
        final PrintWriter writer = response.getWriter();
 
        try {
 
          if (!filePart.getContentType().equals("application/pdf"))
            {
                       writer.println("<br/> Dosya Gecersiz");
                       return;
            }
 
           else if (filePart.getSize()>1048576 ) { //2mb
               {
              writer.println("<br/>Dosya Boyutu çok büyük");
              return;
               }
           }
 
            pdfFileBytes = filePart.getInputStream();  // body'i binary olarak al.
 
            final byte[] bytes = new byte[pdfFileBytes.available()];
             pdfFileBytes.read(bytes);  //binary veri dizisini byte array olarak sakla.
             
 
            Connection  con=null;
             Statement stmt=null;
 
               try {
                     Class.forName("com.mysql.jdbc.Driver");
                     con = DriverManager.getConnection("jdbc:mysql://localhost:3306/document","root","");
                  } catch (Exception e) {
                        System.out.println(e);
                        System.exit(0);
                              }
 
              try {
                  stmt = con.createStatement();
                  //blob alanýyla tablo oluþturmak için
             
 
              } catch (Exception e) {
                        System.out.println("Tablolar zaten mevcut. Bu blogu atlýyor.");
                  }
 
                int success=0;
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO Book VALUES(?,?)");
                pstmt.setString(1, bookId);
                pstmt.setBytes(2,bytes);    //blob alanýna binary veriyi kaydetme
                success = pstmt.executeUpdate();
                if(success>=1)  System.out.println("Kayýtlý Kitap");
                 con.close(); 
 
                 writer.println("<br/> Kitap Baþarýyla Kaydedildi");
 
        } catch (FileNotFoundException fnf) {
            writer.println("Yüklenecek Dosya Belirtmediniz");
            writer.println("<br/> HATA: " + fnf.getMessage());
 
        } catch (SQLException e) {
          
            e.printStackTrace();
        } finally {
 
            if (pdfFileBytes != null) {
                pdfFileBytes.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
 
    }
 
}