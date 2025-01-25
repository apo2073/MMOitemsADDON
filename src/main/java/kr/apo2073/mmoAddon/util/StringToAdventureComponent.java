package kr.apo2073.mmoAddon.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class StringToAdventureComponent {

    public static Component toAdventureComponent(String input) {
        List<Component> components = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        TextColor color = NamedTextColor.WHITE;
        boolean bold = false;
        boolean italic = false;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        boolean isCode = false;

        for (char character : input.toCharArray()) {
            if (character == '§') {
                if (!currentText.isEmpty()) {
                    components.add(createComponent(currentText.toString(), color, bold, italic, underlined, strikethrough, obfuscated));
                    currentText.setLength(0);
                }
                isCode = true;
                continue;
            }

            if (isCode) {
                switch (character) {
                    case 'l' -> bold = true;
                    case 'o' -> italic = true;
                    case 'n' -> underlined = true;
                    case 'm' -> strikethrough = true;
                    case 'k' -> obfuscated = true;
                    case 'r' -> {
                        color = NamedTextColor.WHITE;
                        bold = italic = underlined = strikethrough = obfuscated = false;
                    }
                    case '0' -> color = NamedTextColor.BLACK;
                    case '1' -> color = NamedTextColor.DARK_BLUE;
                    case '2' -> color = NamedTextColor.DARK_GREEN;
                    case '3' -> color = NamedTextColor.DARK_AQUA;
                    case '4' -> color = NamedTextColor.DARK_RED;
                    case '5' -> color = NamedTextColor.DARK_PURPLE;
                    case '6' -> color = NamedTextColor.GOLD;
                    case '7' -> color = NamedTextColor.GRAY;
                    case '8' -> color = NamedTextColor.DARK_GRAY;
                    case '9' -> color = NamedTextColor.BLUE;
                    case 'a' -> color = NamedTextColor.GREEN;
                    case 'b' -> color = NamedTextColor.AQUA;
                    case 'c' -> color = NamedTextColor.RED;
                    case 'd' -> color = NamedTextColor.LIGHT_PURPLE;
                    case 'e' -> color = NamedTextColor.YELLOW;
                    case 'f' -> color = NamedTextColor.WHITE;
                }
                isCode = false;
                continue;
            }

            currentText.append(character);
        }

        if (!currentText.isEmpty()) {
            components.add(createComponent(currentText.toString(), color, bold, italic, underlined, strikethrough, obfuscated));
        }

        return components.stream().reduce(Component.empty(), Component::append);
    }

    private static Component createComponent(String text, TextColor color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated) {
        Style style = Style.style(color)
                .decoration(TextDecoration.BOLD, bold ? TextDecoration.State.TRUE : TextDecoration.State.FALSE)
                .decoration(TextDecoration.ITALIC, italic ? TextDecoration.State.TRUE : TextDecoration.State.FALSE)
                .decoration(TextDecoration.UNDERLINED, underlined ? TextDecoration.State.TRUE : TextDecoration.State.FALSE)
                .decoration(TextDecoration.STRIKETHROUGH, strikethrough ? TextDecoration.State.TRUE : TextDecoration.State.FALSE)
                .decoration(TextDecoration.OBFUSCATED, obfuscated ? TextDecoration.State.TRUE : TextDecoration.State.FALSE);

        return Component.text(text).style(style);
    }
}