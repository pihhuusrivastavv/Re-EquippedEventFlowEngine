package com.anshika.Re_EquippedEventFlowEngine.Web.Response;

import java.time.LocalDateTime;
public class EventErrorResponse
{
    private String msg;
    private int status;
    private LocalDateTime timestamp;

    public EventErrorResponse(String msg, int status, LocalDateTime timestamp)
    {
        this.msg = msg;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMsg()
    {
        return msg;
    }

    public int getStatus()
    {
        return status;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }
}
