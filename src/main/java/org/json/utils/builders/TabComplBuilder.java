package org.json.utils.builders;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.Predicate;

public class TabComplBuilder {
    private final String[] args;
    private Map<Integer, List<String>> entries = new HashMap<>();

    public TabComplBuilder(CommandSender sender, Command command, String alias, String[] args) {
        this.args = args;
    }

    public TabComplBuilder add(int atIndex, String[] entry, Predicate<String[]> condition) {
        if (condition.test(this.args)) {
            this.add(atIndex, entry);
        }

        return this;
    }

    public TabComplBuilder add(int atIndex, String[] entry) {
        atIndex = Math.max(1, atIndex);
        this.entries.put(atIndex, Arrays.stream(entry).toList());
        return this;
    }

    public List<String> build() {
        List<String> list = new ArrayList<>((Collection) (this.entries.get(this.args.length) != null ? (Collection) this.entries.get(this.args.length) : new ArrayList()));
        list.removeIf((s) -> !s.toLowerCase().contains(this.args[this.args.length - 1].toLowerCase()));
        return list;
    }
}
