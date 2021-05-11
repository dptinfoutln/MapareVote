package fr.univtln.mapare.resources;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteQuery {
    int pageIndex;
    int pageSize;
    String exactmatch;
    String prefixmatch;
    String suffixmatch;
    String algoname;
    String sortkey;
    boolean open;
}
