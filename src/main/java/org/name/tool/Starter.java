package org.name.tool;

import org.apache.commons.cli.ParseException;
import org.name.tool.cli.CLIParser;
import org.name.tool.core.Tool;
import org.name.tool.core.ToolInput;

public class Starter {
    public static void main(String[] args) {
        CLIParser cliParser = new CLIParser();
        ToolInput toolInput = null;
        try {
            toolInput = cliParser.parse(args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Tool tool = new Tool(toolInput);
        tool.run();
    }
}
