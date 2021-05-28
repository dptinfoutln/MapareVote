package fr.univtln.mapare.resources;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * The type Vote query.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteQuery {
    String exactmatch;
    String prefixmatch;
    String suffixmatch;
    String algoname;
    String sortkey;
    String order;
    boolean open;
}
