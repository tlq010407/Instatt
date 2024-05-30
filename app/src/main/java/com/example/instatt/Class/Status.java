package com.example.instatt.Class;

import com.example.instatt.R;

public class Status {
    private String status; // Name of the class type (e.g., "Lecture", "Computing", etc.)
    private int iconResourceId; // Resource ID of the associated icon
    public Status(String status, int iconResourceId) {
        this.status = status;
        this.iconResourceId = iconResourceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }
    public String getStatusName() {
        return status;
    }
    public static final Status LOCK = new Status("LOCK", R.drawable.baseline_lock_24);
    public static final Status UNLOCK = new Status("UNLOCK", R.drawable.pen_swirl_svgrepo_com);
    public static final Status SIGNED = new Status("SIGNED", R.drawable.correct_success_tick_svgrepo_com);
    public static final Status ABSENT = new Status("ABSENT", R.drawable.cancel_svgrepo_com);
    public static Status getStatusFromString(String statusString) {
        switch (statusString) {
            case "LOCK":
                return LOCK;
            case "UNLOCK":
                return UNLOCK;
            case "SIGNED":
                return SIGNED;
            case "ABSENT":
                return ABSENT;
            default:
                return null; // Or some default status
        }
    }
}
