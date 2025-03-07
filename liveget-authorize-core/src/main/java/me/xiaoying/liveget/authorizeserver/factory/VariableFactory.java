package me.xiaoying.liveget.authorizeserver.factory;

import java.util.List;

public class VariableFactory {
    private String string;

    public VariableFactory(String string) {
        this.string = string;
    }

    public VariableFactory parameters(List<String> parameters) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < parameters.size(); i++) {
            stringBuilder.append(parameters.get(i));

            if (i == parameters.size() - 1)
                break;

            stringBuilder.append(", ");
        }

        this.string = this.string.replace("%parameters%", stringBuilder.toString());
        return this;
    }

    @Override
    public String toString() {
        return this.string;
    }
}