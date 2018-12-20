package com.liljo.story.verification;

public class Message implements Comparable<Message> {

    private final String message;
    private final Severity severity;

    public Message(String message, Severity severity) {
        this.message = message;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public int compareTo(Message o) {
        final int severityCompare = this.severity.compareTo(o.severity);
        if (severityCompare != 0) {
            return severityCompare;
        }
        return this.message.compareTo(o.message);
    }

    @Override
    public String toString() {
        return String.format("Message{severity=%s, message='%s'}", severity, message);
    }
}
