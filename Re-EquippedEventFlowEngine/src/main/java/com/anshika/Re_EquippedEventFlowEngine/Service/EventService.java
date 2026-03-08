package com.anshika.Re_EquippedEventFlowEngine.Service;

import com.anshika.Re_EquippedEventFlowEngine.Web.Response.EventResponse;


public interface  EventService
{
      void publishEvent(String type,String Payload) throws InterruptedException;
    EventResponse getEvent(int id);
}
