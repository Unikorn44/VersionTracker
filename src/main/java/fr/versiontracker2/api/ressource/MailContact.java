package fr.versiontracker2.api.ressource;

import lombok.Data;

@Data
public class MailContact {
    private String name;
    private String email;
    private String message;
}
