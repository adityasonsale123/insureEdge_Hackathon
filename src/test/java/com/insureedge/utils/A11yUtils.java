package com.insureedge.utils;

import org.openqa.selenium.*;

import java.util.Locale;

public final class A11yUtils {
    private A11yUtils() {}

    public static class Color {
        public final int r, g, b;
        public Color(int r, int g, int b) {
            this.r = clamp(r); this.g = clamp(g); this.b = clamp(b);
        }
        private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }
        @Override public String toString(){ return "rgb(" + r + "," + g + "," + b + ")"; }
    }

    public static Color parseCssColor(String cssColor) {
        cssColor = cssColor.trim().toLowerCase(Locale.ROOT);
        if (cssColor.startsWith("rgb")) {
            String inside = cssColor.substring(cssColor.indexOf("(") + 1, cssColor.indexOf(")"));
            String[] parts = inside.split(",");
            return new Color(
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim())
            );
        }
        if (cssColor.startsWith("#")) {
            String hex = cssColor.substring(1);
            if (hex.length() == 3) {
                hex = "" + hex.charAt(0) + hex.charAt(0)
                        + hex.charAt(1) + hex.charAt(1)
                        + hex.charAt(2) + hex.charAt(2);
            }
            return new Color(
                    Integer.parseInt(hex.substring(0, 2), 16),
                    Integer.parseInt(hex.substring(2, 4), 16),
                    Integer.parseInt(hex.substring(4, 6), 16)
            );
        }
        return new Color(0,0,0);
    }

    public static Color computed(WebElement el, String cssProp) {
        return parseCssColor(el.getCssValue(cssProp));
    }

    public static Color effectiveBackground(WebDriver driver, WebElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script =
                "function bg(el) { while (el) { var s = getComputedStyle(el); var c = s.backgroundColor; " +
                        "if (c && c !== 'rgba(0, 0, 0, 0)' && c !== 'transparent') return c; el = el.parentElement; }" +
                        "return 'rgb(255,255,255)'; } return bg(arguments[0]);";
        String bg = String.valueOf(js.executeScript(script, el));
        return parseCssColor(bg);
    }

    public static double contrastRatio(Color c1, Color c2) {
        double L1 = luminance(c1), L2 = luminance(c2);
        double lighter = Math.max(L1, L2), darker = Math.min(L1, L2);
        return (lighter + 0.05) / (darker + 0.05);
    }

    private static double luminance(Color c) {
        double rs = srgbLinear(c.r / 255.0);
        double gs = srgbLinear(c.g / 255.0);
        double bs = srgbLinear(c.b / 255.0);
        return 0.2126*rs + 0.7152*gs + 0.0722*bs;
    }

    private static double srgbLinear(double v) {
        return (v <= 0.03928) ? (v / 12.92) : Math.pow((v + 0.055) / 1.055, 2.4);
    }

    public static boolean isLikelyNormalText(WebElement el) {
        String tag = el.getTagName() == null ? "" : el.getTagName().toLowerCase(Locale.ROOT);
        String cls = (el.getAttribute("class") == null ? "" : el.getAttribute("class")).toLowerCase(Locale.ROOT);
        boolean isIcon = "i".equals(tag) || cls.contains("bi ");
        if (isIcon) return false;
        double fontPx = parseFontPx(el);
        boolean bold = isBold(el);
        boolean large = fontPx >= 18.66 || (fontPx >= 14 && bold);
        return !large;
    }

    private static double parseFontPx(WebElement el) {
        try {
            String fs = el.getCssValue("font-size"); // e.g. "16px"
            if (fs == null) return 16.0;
            fs = fs.trim().toLowerCase(Locale.ROOT);
            if (fs.endsWith("px")) return Double.parseDouble(fs.replace("px",""));
            return Double.parseDouble(fs.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 16.0;
        }
    }

    private static boolean isBold(WebElement el) {
        try {
            String fw = el.getCssValue("font-weight");
            if (fw == null) return false;
            fw = fw.trim().toLowerCase(Locale.ROOT);
            if ("bold".equals(fw)) return true;
            return Integer.parseInt(fw.replaceAll("[^0-9]","")) >= 700;
        } catch (Exception e) {
            return false;
        }
    }
}