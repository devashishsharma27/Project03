package in.co.sunrays.proj3.util;

public class EmailMessage {
	/**
	 * Contains comma separated TO addresses
	 */
private String to=null;

/**
 * Sender addresses
 */
private String from=null;

/**
 * contains comma separated bcc address
 */
private String bcc=null;

/**
 * contains comma separated cc address
 */
private String cc=null;
/**
 * Contains message subject
 */
private String subject=null;

/**
 * contains message
 */
private String message=null;

/**
 * contains message type:Type of message whether it is Html or text, default is Text
 */
private int messageType = TEXT_MSG;
public static final int HTML_MSG = 1;
public static final int TEXT_MSG = 2;
public String getTo() {
	return to;
}
public void setTo(String to) {
	this.to = to;
}
public String getFrom() {
	return from;
}
public void setFrom(String from) {
	this.from = from;
}
public String getBcc() {
	return bcc;
}
public void setBcc(String bcc) {
	this.bcc = bcc;
}
public String getCc() {
	return cc;
}
public void setCc(String cc) {
	this.cc = cc;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public int getMessageType() {
	return messageType;
}
public void setMessageType(int messageType) {
	this.messageType = messageType;
}
public static int getHtmlMsg() {
	return HTML_MSG;
}
public static int getTextMsg() {
	return TEXT_MSG;
}

}
