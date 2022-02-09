package fr.versiontracker2.traitement.modele;

import java.util.ArrayList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StringList extends ArrayList<String> {
	private static final long serialVersionUID = -7281878506041521559L;
}
