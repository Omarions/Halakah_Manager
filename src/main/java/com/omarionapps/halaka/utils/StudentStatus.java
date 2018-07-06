package com.omarionapps.halaka.utils;

/**
 * Created by Omar on 15-Apr-17.
 */
public enum StudentStatus {
   WAITING("Waiting"), STUDYING("Studying"), CERTIFIED("Certified"), FIRED("Fired"), TEMP_STOP("Temporary Stopped"), FINAL_STOP("Final Stopped");
   private String trackStatus;

   StudentStatus(String trackStatus) {
      this.trackStatus = trackStatus;
   }

   public String getTrackStatus() {
      return trackStatus;
   }
}
