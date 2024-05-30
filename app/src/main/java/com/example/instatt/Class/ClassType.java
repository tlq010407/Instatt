package com.example.instatt.Class;

import com.example.instatt.R;

public class ClassType {
    private String typeName; // Name of the class type (e.g., "Lecture", "Computing", etc.)
    private int iconResourceId; // Resource ID of the associated icon

    // Constructor
    public ClassType(String typeName, int iconResourceId) {
        this.typeName = typeName;
        this.iconResourceId = iconResourceId;
    }

    // Getter methods
    public String getTypeName() {
        return typeName;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
    // Static instances of class types
    public static final ClassType LECTURE = new ClassType("Lecture", R.drawable.university_lecture_svgrepo_com);
    public static final ClassType COMPUTING = new ClassType("Computing", R.drawable.computer_presentation_technology_svgrepo_com);
    public static final ClassType TUTORIAL = new ClassType("Tutorial", R.drawable.lab_worker_profession_experiment_science_svgrepo_com);
    public static final ClassType LAB = new ClassType("Lab", R.drawable.tutorial);

    public static ClassType getTypeFromString(String statusString) {
        switch (statusString) {
            case "LECTURE":
                return LECTURE;
            case "COMPUTING":
                return COMPUTING;
            case "TUTORIAL":
                return TUTORIAL;
            case "LAB":
                return LAB;
            default:
                return null; // Or some default status
        }
    }
}

