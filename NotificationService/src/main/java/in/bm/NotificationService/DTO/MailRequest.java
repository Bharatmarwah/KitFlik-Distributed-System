package in.bm.NotificationService.DTO;

public class MailRequest {
    private String toEmail;
    private String subject;
    private String body;

    public MailRequest() {}

    public MailRequest(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
