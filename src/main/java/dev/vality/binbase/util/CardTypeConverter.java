package dev.vality.binbase.util;

import dev.vality.binbase.domain.CardType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CardTypeConverter {

    public static CardType convertToCardType(String type) {
        return switch (type) {
            case "DEBIT" -> CardType.debit;
            case "CHARGE CARD" -> CardType.charge_card;
            case "CREDIT" -> CardType.credit;
            case "DEBIT OR CREDIT" -> CardType.credit_or_debit;
            default -> null;
        };
    }
}
