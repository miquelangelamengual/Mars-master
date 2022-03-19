package me.imluis_.crates.commons.command;

import java.util.Arrays;

import me.imluis_.crates.commons.command.argument.CrateColorArgument;
import me.imluis_.crates.commons.command.argument.CrateCreateArgument;
import me.imluis_.crates.commons.command.argument.CrateDeleteArgument;
import me.imluis_.crates.commons.command.argument.CrateGiveKeyArgument;
import me.imluis_.crates.commons.command.argument.CrateItemArgument;
import me.imluis_.crates.commons.command.argument.CrateKeyArgument;
import me.imluis_.crates.commons.command.argument.CrateListArgument;
import me.imluis_.crates.commons.command.argument.CrateLootChestArgument;
import me.imluis_.crates.commons.command.argument.CrateSaveArgument;
import me.imluis_.crates.commons.command.argument.CrateVersionArgument;
import me.imluis_.crates.util.command.ArgumentExecutor;

public class CrateCommand extends ArgumentExecutor {

	public CrateCommand() {
		super("crate");

		Arrays.asList(
				new CrateCreateArgument(),
				new CrateDeleteArgument(),
				new CrateGiveKeyArgument(),
				new CrateItemArgument(),
				new CrateKeyArgument(),
				new CrateListArgument(),
				new CrateLootChestArgument(),
				new CrateColorArgument(),
				new CrateSaveArgument(),
				new CrateVersionArgument()
				).stream().forEach(commandArgument -> this.addArgument(commandArgument));
	}
}
