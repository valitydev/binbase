package dev.vality.binbase.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinData {

    @EqualsAndHashCode.Exclude
    private Long id;

    private String paymentSystem;
    private String bankName;
    private CountryCode isoCountryCode;
    private CardType cardType;
    private String category;
}
