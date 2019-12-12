package io.github.jorelali.commandapi.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;
import io.github.jorelali.commandapi.api.executors.ExecutorType;
import io.github.jorelali.commandapi.api.executors.IExecutorN;
import io.github.jorelali.commandapi.api.executors.IExecutorR;

class CustomCommandExecutor {
	
	private List<IExecutorN<? extends CommandSender>> ex;
	private List<IExecutorR<? extends CommandSender>> rEx;
	
	public CustomCommandExecutor() {
		ex = new ArrayList<>();
		rEx = new ArrayList<>();
	}
	
	public void addNormalExecutor(IExecutorN<? extends CommandSender> ex) {
		this.ex.add(ex);
	}
	
	public void addResultingExecutor(IExecutorR<? extends CommandSender> rEx) {
		this.rEx.add(rEx);
	}
	
	public int execute(CommandSender sender, Object[] arguments) throws CommandSyntaxException {
		
		//Parse executor type
        if (!rEx.isEmpty()) {
            //Run resulting executor
            try {
                return executeResultingExecutor(sender, arguments);
            } catch (WrapperCommandSyntaxException e) {
                throw e.getException();
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return 0;
            }
        } else {
            //Run normal executor
            try {
                return executeNormalExecutor(sender, arguments);
            } catch (WrapperCommandSyntaxException e) {
                throw e.getException();
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return 0;
            }
        }
	}
	
	private int executeNormalExecutor(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		if(sender instanceof Player && ex.stream().anyMatch(o -> o.getType() == ExecutorType.PLAYER)) {
			return ex.stream().filter(o -> o.getType() == ExecutorType.PLAYER).findFirst().get().executeWith(sender, args);
		} else if(sender instanceof Entity && ex.stream().anyMatch(o -> o.getType() == ExecutorType.ENTITY)) {
			return ex.stream().filter(o -> o.getType() == ExecutorType.ENTITY).findFirst().get().executeWith(sender, args);
		} else if(sender instanceof ConsoleCommandSender && ex.stream().anyMatch(o -> o.getType() == ExecutorType.CONSOLE)) {
			return ex.stream().filter(o -> o.getType() == ExecutorType.CONSOLE).findFirst().get().executeWith(sender, args);
		} else if(sender instanceof BlockCommandSender && ex.stream().anyMatch(o -> o.getType() == ExecutorType.BLOCK)) {
			return ex.stream().filter(o -> o.getType() == ExecutorType.BLOCK).findFirst().get().executeWith(sender, args);
		} else if(ex.stream().anyMatch(o -> o.getType() == ExecutorType.ALL)) {
			return ex.stream().filter(o -> o.getType() == ExecutorType.ALL).findFirst().get().executeWith(sender, args);
		} else {
			throw new WrapperCommandSyntaxException(
				new SimpleCommandExceptionType(
					new LiteralMessage("This command has no implementations for " + sender.getClass().getSimpleName().toLowerCase())
				).create()
			); 
		}
	}
	
	private int executeResultingExecutor(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
		if(sender instanceof Player && rEx.stream().anyMatch(o -> o.getType() == ExecutorType.PLAYER)) {
			return rEx.stream().filter(o -> o.getType() == ExecutorType.PLAYER).findFirst().get().executeWith(sender, args);
		} else if(sender instanceof Entity && rEx.stream().anyMatch(o -> o.getType() == ExecutorType.ENTITY)) {
			return rEx.stream().filter(o -> o.getType() == ExecutorType.ENTITY).findFirst().get().executeWith(sender, args);
		} else if(sender instanceof ConsoleCommandSender && rEx.stream().anyMatch(o -> o.getType() == ExecutorType.CONSOLE)) {
			return rEx.stream().filter(o -> o.getType() == ExecutorType.CONSOLE).findFirst().get().executeWith(sender, args);
		} else if(sender instanceof BlockCommandSender && rEx.stream().anyMatch(o -> o.getType() == ExecutorType.BLOCK)) {
			return rEx.stream().filter(o -> o.getType() == ExecutorType.BLOCK).findFirst().get().executeWith(sender, args);
		} else if(rEx.stream().anyMatch(o -> o.getType() == ExecutorType.ALL)) {
			return rEx.stream().filter(o -> o.getType() == ExecutorType.ALL).findFirst().get().executeWith(sender, args);
		} else {
			throw new WrapperCommandSyntaxException(
				new SimpleCommandExceptionType(
					new LiteralMessage("This command has no implementations for " + sender.getClass().getSimpleName().toLowerCase())
				).create()
			); 
		}
	}
}