package com.nals.hrm.service.impl;

import com.nals.hrm.dto.DaysOffDTO;
import com.nals.hrm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendEmail(String to, String cc, String subject, String body) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
        helper.setTo(to);
        if (cc != null) helper.setCc(cc);
        helper.setSubject(subject);

        helper.setText(body, true);
        javaMailSender.send(msg);
    }

    @Override
    public String mailContent(List<DaysOffDTO> daysOffDTOList) {
        String result = "<head>" +
                "<style>" +
                "      * {" +
                "         margin: auto;" +
                "         padding: 0;" +
                "      }" +
                "" +
                "      .container {" +
                "         text-align: center;" +
                "      }" +
                "" +
                "      .main-content {" +
                "         width: 100%;" +
                "      }" +
                "" +
                "      #main-table {" +
                "         margin-top: 30px;" +
                "         width: 50%;" +
                "         margin-bottom: 30px;" +
                "         border-collapse: collapse;" +
                "      }" +
                "" +
                "      #detail-table {" +
                "         font-weight: normal;" +
                "         width: 80%;" +
                "         border-collapse: collapse;" +
                "      }" +
                "" +
                "      td, th {" +
                "         border: 1px solid #dddddd;" +
                "         text-align: left;" +
                "         padding: 8px;" +
                "      }" +
                "" +
                "      th {" +
                "         background-color: #58a6ff;" +
                "      }" +
                "" +
                "      img {" +
                "         width: 10%;" +
                "         margin-left: 10%;" +
                "      }" +
                "   </style>" +
                "</head>" +
                "<body>" +
                "   <header><img src=\"https://bit.ly/3q60rdy\" style=\"width: 15%\" /></header>" +
                "   <main>" +
                "      <div class=\"container\">" +
                "         <h1><strong>Day off of User: " + daysOffDTOList.get(0).getFullName() + "</strong></h1>" +
                "      </div>" +
                "      <div class=\"main-content\">" +
                "         <div class=\"container\">" +
                "            <table id=\"main-table\">" +
                "               <tbody>" +
                "                  <tr>" +
                "                     <th>User Name</th>" +
                "                     <td>" + daysOffDTOList.get(0).getFullName() + "</td>" +
                "                  </tr>" +
                "                  <tr>" +
                "                     <th>User Email</th>" +
                "                     <td>" + daysOffDTOList.get(0).getEmail() + "</td>" +
                "                  </tr>" +
                "                  <tr>" +
                "                     <th>Reason</th>" +
                "                     <td>" + daysOffDTOList.get(0).getReasons() + "</td>" +
                "                  </tr>" +
                "                  <tr>" +
                "                     <th>Notes</th>" +
                "                     <td>" + daysOffDTOList.get(0).getNotes() + "</td>" +
                "                  </tr>" +
                "               </tbody>" +
                "            </table>" +
                "            <h2><strong>Day off detail: <strong></h2>" +
                "            <table id=\"detail-table\">" +
                "               <tr>" +
                "                  <th>No.</th>" +
                "                  <th>Vacation day</th>" +
                "                  <th>Session day off</th>" +
                "                  <th>Vacation type</th>" +
                "               </tr>";
            for (int i=0; i<daysOffDTOList.size(); i++) {
                result +=
                "               <tr>" +
                "                  <td>" + (i+1) + "</td>" +
                "                  <td>" + daysOffDTOList.get(i).getVacationDay() + "</td>" +
                "                  <td>" + daysOffDTOList.get(i).getSessionDayOffName() + "</td>" +
                "                  <td>" + daysOffDTOList.get(i).getVacationTypeName() + "</td>" +
                "               </tr>";
            }
                result +=
                "            </table>" +
                "            <br />" +
                "         </div>" +
                "      </div>" +
                "   </main>" +
                "</body>";
        return result;
    }
}
