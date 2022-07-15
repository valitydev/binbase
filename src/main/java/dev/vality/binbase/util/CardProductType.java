package dev.vality.binbase.util;

import dev.vality.binbase.domain.CardType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum CardProductType {

    //DEBIT
    MPJ("MPJ"), //Prepaid Mastercard Debit Voucher Meal/Food Card
    MPO("MPO"), //Mastercard Prepaid Debit Standard-Other
    MPN("MPN"), //Mastercard Prepaid Debit Standard-Insurance
    MCD("MCD"), //MIR Classic Debit Overdraft
    MDJ("MDJ"), //Debit Other 2 Embossed
    MDS("MDS"), //Debit Mastercard Card
    MDG("MDG"), //Gold Debit Mastercard Card
    TWB("TWB"), //World Mastercard Black Edition - Immediate Debit
    TCS("TCS"), //Mastercard Standard Card-Immediate Debit
    MBD("MBD"), //Mastercard Professional Debit BusinessCard Card
    MDP("MDP"), //Platinum Debit Mastercard Card
    MDO("MDO"), //Debit Other
    SAP("SAP"), //Platinum Mastercard Salary-Immediate Debit
    MDH("MDH"), //Debit Other Embossed Mastercard Card
    OLS("OLS"), //Maestro-Delayed Debit
    TNW("TNW"), //Mastercard New World-Immediate Debit
    TCC("TCC"), //Mastercard (mixed BIN)-Immediate Debit
    MDT("MDT"), //MIR Debit
    MET("MET"), //Titanium Debit Mastercard Card
    MDB("MDB"), //MIR Debit Business
    ACS("ACS"), //Digital Debit
    TCG("TCG"), //Gold Mastercard Card-Immediate Debit
    MIU("MIU"), //Debit Mastercard Unembossed (Non-U.S.)
    MPT("MPT"), //Mastercard Prepaid Debit Standard-Teen
    TCW("TCW"), //World Signia Mastercard Card-Immediate Debit
    MPD("MPD"), //MIR Premium Debit Overdraft
    TPL("TPL"), //Platinum Mastercard-Immediate Debit
    MDW("MDW"), //World Elite Debit Mastercard / Mastercard Black Debit
    TCE("TCE"), //Mastercard Electronic-Immediate Debit

    //CREDIT
    MCC("MCC"), //Mastercard Credit Card (mixed BIN)
    MPS("MPS"), //MIR Premium Credit Business
    MPC("MPC"), //MIR Premium Credit

    //CREDIT_OR_DEBIT
    P("P"),        //Visa Gold
    MCT("MCT"), //Titanium Mastercard Card
    PRD("PRD"), //MIR Advanced
    NPD("NPD"), //MIR Premium
    CCD("CCD"), //MIR Classic
    N("N"),        //Visa Platinum
    UCG("UCG"), //Union Pay Consumer Gold
    MBK("MBK"), //Mastercard Black Card
    JC("JC"),    //JCB
    MCP("MCP"), //MIR Privilege
    FOUR("4"),    //UnionPay Platium
    N2("N2"),    //Visa Select
    S2("S2"),    //Visa Government Purchasing
    I("I"),        //Visa Infinite
    B("B"),        //Visa Traditional Rewards
    A("A"),        //Visa Traditional
    MPL("MPL"), //Platinum Mastercard Card
    MCS("MCS"), //Standard Mastercard Card
    F("F"),        //Visa Classic
    MCG("MCG"), //Gold Mastercard Card
    MCW("MCW"), //World Mastercard Card
    MCE("MCE"), //Mastercard Electronic Card
    MWE("MWE"), //World Elite Mastercard Card
    MNW("MNW"), //Mastercard World Card
    WBE("WBE"), //World Mastercard Black Edition
    MSI("MSI"), //Maestro Card

    //CHARGE_CARD
    MBP("MBP"), //Mastercard Corporate Prepaid
    WPD("WPD"), //World Prepaid Debit
    MRL("MRL"), //Prepaid Mastercard Electronic Commercial Card (Non-U.S.)
    MTP("MTP"), //Mastercard Platinum Prepaid Travel Card
    MSM("MSM"), //Maestro Prepaid Consumer Promotion Card
    MPY("MPY"), //Mastercard Prepaid Debit Standard-Employee Incentive
    MPM("MPM"), //Mastercard Prepaid Debit Standard-Consumer Incentive
    MRG("MRG"), //Mastercard Prepaid Card (Non-U.S.)
    MPG("MPG"), //Debit Mastercard Standard Prepaid-General Spend
    MHA("MHA"), //Mastercard Healthcare Prepaid Non-Tax
    MPV("MPV"), //Mastercard Prepaid Debit Standard-Government
    MBB("MBB"), //Mastercard Prepaid Consumer
    MRJ("MRJ"), //Prepaid Mastercard Voucher Meal/Food Card
    MGS("MGS"), //Platinum Mastercard Prepaid General Spend
    MRC("MRC"), //Prepaid Mastercard Electronic Card (Non-U.S.)
    MSG("MSG"), //Prepaid Maestro Consumer Reloadable Card
    MPX("MPX"), //Mastercard Prepaid Debit Standard-Flex Benefit
    CDP("CDP"), //MIR Classic Prepaid
    MBC("MBC"), //Mastercard Prepaid Voucher
    MRD("MRD"), //Platinum Debit Mastercard Prepaid General Spend
    MSJ("MSJ"), //Maestro Prepaid Voucher Meal and Food Card
    MPF("MPF"), //Mastercard Prepaid Debit Standard-Gift
    MPP("MPP"), //Mastercard Prepaid Card
    PRP("PRP"), //MIR Advanced Prepaid
    MPR("MPR"), //Mastercard Prepaid Debit Standard-Travel
    MRH("MRH"), //Mastercard Platinum Prepaid Travel Card
    MSO("MSO"), //Maestro Prepaid Other Card
    SUR("SUR"), //Prepaid Unembossed Mastercard Card (Non-U.S.)
    MAQ("MAQ"), //Mastercard Prepaid Commercial Payments Account
    MPA("MPA"), //Mastercard Prepaid Debit Standard-Payroll
    MIP("MIP"), //Prepaid Debit Mastercard Student Card

    //BUSINESS
    MCO("MCO"), //Mastercard Corporate Card
    MBE("MBE"), //Mastercard Electronic Business Card
    NPB("NPB"), //MIR Premium Business
    MRW("MRW"), //Prepaid Mastercard Business Card (Non-U.S.)
    MPB("MPB"), //Mastercard Preferred Business Card
    MSW("MSW"), //Prepaid Maestro Corporate Card
    CCB("CCB"), //MIR Classic Business
    MWB("MWB"), //World Mastercard for Business Card
    MAB("MAB"), //World Elite Mastercard for Business Card
    TCO("TCO"), //Mastercard Corporate-Immediate Debit
    MEO("MEO"), //Mastercard Corporate Executive Card
    MSB("MSB"), //Maestro Small Business Card
    TCB("TCB"), //Mastercard Business Card-Immediate Debit
    MWO("MWO"), //World Elite Mastercard Corporate Card
    CPB("CPB"), //MIR Privilege Business
    PPB("PPB"), //MIR Privilege Plus Business
    BPD("BPD"), //World Debit Business Card
    MEB("MEB"), //Mastercard Executive BusinessCard Card
    MAC("MAC"), //Mastercard World Elite Corporate Card
    MCB("MCB"), //Mastercard BusinessCard Card
    MPW("MPW"), //Debit Mastercard BusinessCard Prepaid Workplace Business to Business
    G1("G1"), //Visa Signature Business
    G3("G3"), //Visa Platinum Business
    G4("G4"), //Visa Infinite Business
    K("K"), //Visa Corporate T&E
    G("G"), //Visa Business

    //UNKNOWN
    MHH("MHH"), //Mastercard HSA Non-Substantiated
    MGF("MGF"), //Mastercard Government Commercial Card
    UCD("UCD"), //Union Pay Consumer Diamond
    UBG("UBG"), //Union Pay Commercial Gold
    MCF("MCF"), //Mastercard Fleet Card
    UCP("UCP"), //Union Pay Consumer Platinum
    MLF("MLF"), //Mastercard Agro Card
    UCC("UCC"), //Union Pay Consumer Classic
    MRO("MRO"), //Mastercard Rewards Only
    CIR("CIR"), //Cirrus
    MLA("MLA"), //Mastercard Central Travel Solutions Air Card
    MLL("MLL"), //Mastercard Central Travel Solutions Land Card
    MLE("MLE"), //Mastercard Brazil General Benefits
    UBC("UBC"), //Union Pay Commercial Classic
    TKN("TKN"), //MIR Token
    MRF("MRF"), //European Regulated Individual Pay
    MHB("MHB"), //Mastercard HSA Substantiated
    MLD("MLD"), //Mastercard Distribution Card
    MLB("MLB"), //Mastercard Brazil Benefit for Home Improvement
    MAP("MAP"), //Mastercard Commercial Payments Account
    MBS("MBS"), //Mastercard B2B Product
    CPP("CPP"), //MIR Privilege Plus
    NAS("NAS"), //No Account Specified
    N1("N1"), //Visa Rewards
    S1("S1"), //Visa Purchasing with Fleet
    J3("J3"), //Visa Healthcare
    Q2("Q2"), //Private Label Basic
    Q4("Q4"), //Private Label Enhanced
    S4("S4"), //Government Services Loan
    Q5("Q5"), //Private Label Specialized
    Q6("Q6"), //Private Label Premium
    I1("I1"), //Visa Infinite Privilege
    K1("K1"), //Visa Government Corporate T&E
    S5("S5"), //Commercial Transport EBT
    S3("S3"), //Visa Government Purchasing With Fleet
    I2("I2"), //Visa Ultra High Net Worth
    S6("S6"), //Business Loan
    Q3("Q3"), //Private Label Standard
    C("C"), //Visa Signature
    X("X"), //B2B Virtual Payments
    FIVE("5"), //UnionPay Diamond
    ONE("1"), //UnionPay Classic
    V("V"), //V PAY
    L("L"), //Electron
    THREE("3"), //UnionPay Gold
    ZERO("0"), //CUP Unknown Product
    E("E"), //Proprietary ATM
    S("S"), //Visa Purchasing
    UNKNOWN("UNKNOWN"); //MC Unknown Product


    private static final CardProductType[] DEBIT = {
            MPJ, MCB, MPO, MPN, MCD, MDJ, MDS, MDG, TWB, TCS, MBD, MDP, MDO, SAP, MDH, OLS, TNW, TCC, MDT, MET, MDB,
            ACS, TCG, MIU, MPT, MDT, TCW, MPD, TPL, MDW, TCE
    };

    private static final CardProductType[] CREDIT = {
            MCC, MPS, MPC
    };

    private static final CardProductType[] CHARGE_CARD = {
            MDP, MBP, WPD, MRL, MTP, MSM, MPY, MPM, MRG, MPG, MHA, MPD, MPV, MBB, MRJ, MGS, MRC, MSG, MPX, CDP, MBC,
            MRD, MPP, MSJ, MPF, MPP, PRP, MPR, MRH, MSO, SUR, MAQ, MPA, MIP
    };

    private static final CardProductType[] CREDIT_OR_DEBIT = {
            P, MCT, PRD, NPD, CCD, N, UCG, MBK, JC, MCP, FOUR, N2, S2, I, B, A, MPL, MCS, F, MCG, MCW, MCE, MWE, MNW,
            WBE, MSI
    };
    private static final Map<String, CardProductType> valueMap = new HashMap<>();

    static {
        for (CardProductType cpt : values()) {
            valueMap.put(cpt.value, cpt);
        }
    }

    private final String value;

    public static CardType getByValue(String value) {
        return convertValueToCardType(valueMap.get(value));
    }

    public static CardType convertValueToCardType(CardProductType value) {
        if (Arrays.asList(CardProductType.CREDIT).contains(value)) {
            return CardType.credit;
        } else if (Arrays.asList(CardProductType.DEBIT).contains(value)) {
            return CardType.debit;
        } else if (Arrays.asList(CardProductType.CHARGE_CARD).contains(value)) {
            return CardType.charge_card;
        } else if (Arrays.asList(CardProductType.CREDIT_OR_DEBIT).contains(value)) {
            return CardType.credit_or_debit;
        }
        return null;
    }
}

