/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.sample.template.payroll.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author samueladebowale
 */
@Getter
@Configuration
public class CurrencyFactory {
    
    @Value("${api.default.currency}")
    private String defaultCurrency;

    public static final CurrencyUnit USD = Monetary.getCurrency("USD");

    public enum Currency {
        AED("AED"), AFN("AFN"), ALL("ALL"), AMD("AMD"), ANG("ANG"), AOA("AOA"), ARS("ARS"), AUD("AUD"), AWG("AWG"), AZN("AZN"), BAM("BAM"), BBD("BBD"),
        BDT("BDT"), BGN("BGN"), BHD("BHD"), BIF("BIF"), BMD("BMD"), BND("BND"), BOB("BOB"), BOV("BOV"), BRL("BRL"), BSD("BSD"), BTN("BTN"), BWP("BWP"),
        BYN("BYN"), BYR("BYR"), BZD("BZD"), CAD("CAD"), CDF("CDF"), CHE("CHE"), CHF("CHF"), CHW("CHW"), CLF("CLF"), CLP("CLP"), CNY("CNY"), COP("COP"),
        COU("COU"), CRC("CRC"), CUC("CUC"), CUP("CUP"), CVE("CVE"), CZK("CZK"), DJF("DJF"), DKK("DKK"), DOP("DOP"), DZD("DZD"), EGP("EGP"), ERN("ERN"),
        ETB("ETB"), EUR("EUR"), FJD("FJD"), FKP("FKP"), GBP("GBP"), GEL("GEL"), GHS("GHS"), GIP("GIP"), GMD("GMD"), GNF("GNF"), GTQ("GTQ"), GYD("GYD"),
        HKD("HKD"), HNL("HNL"), HRK("HRK"), HTG("HTG"), HUF("HUF"), IDR("IDR"), ILS("ILS"), INR("INR"), IQD("IQD"), IRR("IRR"), ISK("ISK"), JMD("JMD"),
        JOD("JOD"), JPY("JPY"), KES("KES"), KGS("KGS"), KHR("KHR"), KMF("KMF"), KPW("KPW"), KRW("KRW"), KWD("KWD"), KYD("KYD"), KZT("KZT"), LAK("LAK"),
        LBP("LBP"), LKR("LKR"), LRD("LRD"), LSL("LSL"), LYD("LYD"), MAD("MAD"), MDL("MDL"), MGA("MGA"), MKD("MKD"), MMK("MMK"), MNT("MNT"), MOP("MOP"),
        MRO("MRO"), MUR("MUR"), MVR("MVR"), MWK("MWK"), MXN("MXN"), MXV("MXV"), MYR("MYR"), MZN("MZN"), NAD("NAD"), NGN("NGN"), NIO("NIO"), NOK("NOK"),
        NPR("NPR"), NZD("NZD"), OMR("OMR"), PAB("PAB"), PEN("PEN"), PGK("PGK"), PHP("PHP"), PKR("PKR"), PLN("PLN"), PYG("PYG"), QAR("QAR"), RON("RON"),
        RSD("RSD"), RUB("RUB"), RWF("RWF"), SAR("SAR"), SBD("SBD"), SCR("SCR"), SDG("SDG"), SEK("SEK"), SGD("SGD"), SHP("SHP"), SLL("SLL"), SOS("SOS"),
        SRD("SRD"), SSP("SSP"), STD("STD"), SYP("SYP"), SZL("SZL"), THB("THB"), TJS("TJS"), TMT("TMT"), TND("TND"), TOP("TOP"), TRY("TRY"), TTD("TTD"),
        TWD("TWD"), TZS("TZS"), UAH("UAH"), UGX("UGX"), USD("USD"), USN("USN"), UYI("UYI"), UYU("UYU"), UZS("UZS"), VEF("VEF"), VND("VND"), VUV("VUV"),
        WST("WST"), XAF("XAF"), XAG("XAG"), XAU("XAU"), XBA("XBA"), XBB("XBB"), XBC("XBC"), XBD("XBD"), XCD("XCD"), XDR("XDR"), XFU("XFU"), XOF("XOF"),
        XPD("XPD"), XPF("XPF"), XPT("XPT"), XSU("XSU"), XTS("XTS"), XUA("XUA"), XXX("XXX"), YER("YER"), ZAR("ZAR"), ZMW("ZMW");

        private String iso4217Code = "";

        Currency(String code) {
            this.iso4217Code = code;
        }

        @Override
        public String toString() {
            return iso4217Code;
        }

        // Implementing a fromString method on an enum type
        private static final Map<String, Currency> stringToEnum = new HashMap<>();

        static { // Initialize map from constant name to enum constant
            for (Currency op : values()) {
                stringToEnum.put(op.toString(), op);
            }
        }

        // Returns Operation for string, or null if string is invalid
        public static Currency fromString(String typeName) {
            return stringToEnum.get(StringUtils.upperCase(typeName));
        }

        public static Collection<Currency> getAllTypes() {
            return stringToEnum.values();
        }

        public static Set<String> getAllNames() {
            return new TreeSet<>(stringToEnum.keySet());
        }
    }
}
