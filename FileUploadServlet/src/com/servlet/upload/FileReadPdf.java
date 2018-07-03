package com.servlet.upload;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 

@WebServlet("/FileReadPdf")
public class FileReadPdf extends HttpServlet {
    private static final long serialVersionUID = 1L;
        
  
    public FileReadPdf() {
        super();       
    }
 
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         
        String bookId = request.getParameter("bookId")!=null?request.getParameter("bookId"):"NA";
         
        ServletOutputStream sos;
        Connection  con=null;
        PreparedStatement pstmt=null;
         
        response.setContentType("application/pdf");
 
        response.setHeader("Content-disposition","inline; filename="+bookId+".pdf" );
 
 
         sos = response.getOutputStream();
         
 
           try {
               Class.forName("com.mysql.jdbc.Driver");
               con = DriverManager.getConnection("jdbc:mysql://localhost:3306/document","root","");
          } catch (Exception e) {
                     System.out.println(e);
                     System.exit(0); 
                          }
            
          ResultSet rset=null;
            try {
                pstmt = con.prepareStatement("Select bookcontent from Book where bookId=?");
                pstmt.setString(1, bookId.trim());
                rset = pstmt.executeQuery();
                if (rset.next())
                    sos.write(rset.getBytes("bookcontent"));
                else
                    return;
                 
            } catch (SQLException e) {
                
                e.printStackTrace();
            }
     
        sos.flush();
        sos.close();
         
    }
 
   
}
