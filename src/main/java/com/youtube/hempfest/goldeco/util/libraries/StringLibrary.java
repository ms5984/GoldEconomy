package com.youtube.hempfest.goldeco.util.libraries;

import com.youtube.hempfest.goldeco.GoldEconomy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class StringLibrary {
    private static StringLibrary instance;
    private final Properties p;

    private StringLibrary() {
        instance = this;
        this.p = new Properties();
    }
    public StringLibrary(GoldEconomy goldEconomy) {
        this();
        try {
            final InputStream resource = Objects.requireNonNull(goldEconomy.getResource("messages.properties"));
            p.load(new InputStreamReader(resource, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public StringLibrary(GoldEconomy goldEconomy, String region) {
        this();
        try {
            final InputStream resource = goldEconomy.getResource("lang/messages".concat(region).concat(".properties"));
            p.load(new InputStreamReader(Objects.requireNonNull(resource), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String buyList1(int page) {
        return Objects.requireNonNull(instance.p.getProperty("buy-list.pages"))
                .replaceAll("%page%", String.valueOf(page));
    }

    public static String buyHover(String nextTop) {
        return instance.p.getProperty("buy-list.hover").replaceAll("%nextTop%", nextTop);
    }

    public static String sellList1(int page) {
        return Objects.requireNonNull(instance.p.getProperty("sell-list.pages"))
                .replaceAll("%page%", String.valueOf(page));
    }

    public static String sellHover(String nextTop) {
        return instance.p.getProperty("sell-list.hover").replaceAll("%nextTop%", nextTop);
    }

    public enum Lang {
        PREFIX("PREFIX"),
        navigate("navigate"),
        NEXT("word.NEXT"),
        BACK("word.BACK"),
        ListToNextPage("list.to-next"),
        ListGoBack("list.go-back"),
        ListEmpty("empty-list"),
        OnlyOnePage("pages.single");

        private final String s;

        Lang(String key) {
            this.s = key;
        }

        public String get() {
            return instance.p.getProperty(s);
        }
    }

    public static String pagesLeft(int count) {
        return instance.p.getProperty("pages.plural").replaceAll("%totalPageCount%", String.valueOf(count));
    }

    public enum Menu {
        Title("menu.title"),
        Horizontal_Rule("menu.hr"),
        Line1("menu.1"),
        Line2("menu.2"),
        Line3("menu.3"),
        Line4("menu.4"),
        Line5("menu.5"),
        Line6("menu.6"),
        Staff1("menu.s1"),
        Staff2("menu.s2"),
        Staff3("menu.s3"),
        Staff4("menu.s4"),
        Staff5("menu.s5");

        private final String s;

        Menu(String s) {
            this.s = s;
        }

        public String get() {
            return instance.p.getProperty(s);
        }
    }

    public static String invalidDouble() {
        return instance.p.getProperty("invalid-double");
    }

    public static String invalidInteger() {
        return instance.p.getProperty("invalid-integer");
    }

    public static String notEnoughMoney(String world) {
        return Objects.requireNonNull(instance.p.getProperty("not-enough-money")).replaceAll("%world%", world);
    }

    public static String notEnoughSpace() {
        return instance.p.getProperty("not-enough-space");
    }

    public static String amountTooLarge() {
        return instance.p.getProperty("amount-too-large");
    }

    public static String nameUnknown(String replacement) {
        return Objects.requireNonNull(instance.p.getProperty("name-unknown"))
                .replaceAll("%args%", replacement);
    }

    public static String nameNeeded() {
        return instance.p.getProperty("name-needed");
    }

    public static String money(String world, String amount, String currency) {
        return Objects.requireNonNull(instance.p.getProperty("money"))
                .replaceAll("%world%", world)
                .replaceAll("%amount%", amount)
                .replaceAll("%currency%", currency);
    }

    public static String moneySent(String amount, String recipientName) {
        return Objects.requireNonNull(instance.p.getProperty("money-sent"))
                .replaceAll("%amount%", amount).replaceAll("%player%", recipientName);
    }

    public static String moneySet(String amount) {
        return Objects.requireNonNull(instance.p.getProperty("money-set")).replaceAll("%amount%", amount);
    }

    public static String moneyReceived(String senderName, String amount) {
        return Objects.requireNonNull(instance.p.getProperty("money-received"))
                .replaceAll("%player%", senderName).replaceAll("%amount%", amount);
    }

    public static String moneyTaken(String amount, String balance) {
        return Objects.requireNonNull(instance.p.getProperty("money-taken"))
                .replaceAll("%amount%", amount).replaceAll("%balance%", balance);
    }

    public static String moneyGiven(String amount, String balance) {
        return Objects.requireNonNull(instance.p.getProperty("money-given"))
                .replaceAll("%amount%", amount)
                .replaceAll("%balance%", balance);
    }

    public static String moneyDeposited(String amount) {
        return Objects.requireNonNull(instance.p.getProperty("money-deposited"))
                .replaceAll("%amount%", amount);
    }

    public static String moneyWithdrawn(String amount) {
        return Objects.requireNonNull(instance.p.getProperty("money-withdrawn"))
                .replaceAll("%amount%", amount);
    }

    public static String playerNotFound() {
        return instance.p.getProperty("player-not-found");
    }

    public static String maxWithdrawReached() {
        return instance.p.getProperty("max-withdraw-reached");
    }

    public static String accountMade(String account, String accountWorld) {
        return Objects.requireNonNull(instance.p.getProperty("account-made"))
                .replaceAll("%account%", account)
                .replaceAll("%account_world%", accountWorld);
    }

    public static String accountDeposit(String amount, String account) {
        return Objects.requireNonNull(instance.p.getProperty("account-deposit"))
                .replaceAll("%amount%", amount)
                .replaceAll("%account%", account);
    }

    public static String accountWithdraw(String amount, String account) {
        return Objects.requireNonNull(instance.p.getProperty("account-withdraw"))
                .replaceAll("%amount%", amount)
                .replaceAll("%account%", account);
    }

    public static String accountAlreadyMade() {
        return instance.p.getProperty("account-already-made");
    }

    public static String accountNotAllowed() {
        return instance.p.getProperty("account-not-allowed");
    }

    public static String accountBalanceSet(String account, String newBalance) {
        return Objects.requireNonNull(instance.p.getProperty("account-balance-set"))
                .replaceAll("%account%", account)
                .replaceAll("%balance%", newBalance);
    }

    public static String staffAccountSet(String account, String amount) {
        return Objects.requireNonNull(instance.p.getProperty("staff-account-set"))
                .replaceAll("%account%", account)
                .replaceAll("%amount%", amount);
    }

    public static String staffMoneySet(String player, String amount) {
        return Objects.requireNonNull(instance.p.getProperty("staff-money-set"))
                .replaceAll("%player%", player)
                .replaceAll("%amount%", amount);
    }

    public static String staffMoneyGiven(String amount, String player) {
        return Objects.requireNonNull(instance.p.getProperty("staff-money-given"))
                .replaceAll("%amount%", amount)
                .replaceAll("%player%", player);
    }

    public static String staffMoneyTaken(String amount, String player) {
        return Objects.requireNonNull(instance.p.getProperty("staff-money-taken"))
                .replaceAll("%amount%", amount)
                .replaceAll("%player%", player);
    }

}
