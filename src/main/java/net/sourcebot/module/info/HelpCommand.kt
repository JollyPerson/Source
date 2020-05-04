package net.sourcebot.module.info

import net.dv8tion.jda.api.entities.Message
import net.sourcebot.api.alert.Alert
import net.sourcebot.api.command.Command
import net.sourcebot.api.command.CommandMap
import net.sourcebot.api.command.argument.ArgumentInfo
import net.sourcebot.api.command.argument.Arguments
import net.sourcebot.api.command.argument.OptionalArgument
import net.sourcebot.api.module.SourceModule
import net.sourcebot.module.info.alert.CommandInfoAlert
import net.sourcebot.module.info.alert.ModuleInfoAlert
import net.sourcebot.module.info.alert.ModuleListAlert
import net.sourcebot.module.info.alert.UnknownTopicAlert

class HelpCommand(
    private val modules: Set<SourceModule>,
    private val commandMap: CommandMap
) : Command() {
    override val name = "help"
    override val description = "Show command / module help information"
    override val argumentInfo = ArgumentInfo(
        OptionalArgument("module|command", "The command / module to show help information for")
    )

    override fun execute(message: Message, args: Arguments): Alert {
        val topic = args.next() ?: return ModuleListAlert(modules)
        var command = commandMap[topic]
        if (command != null) {
            command = command.cascade(message, args)
            return CommandInfoAlert(command)
        }
        val module = modules.find { it.name.equals(topic, true) }
        if (module != null) return ModuleInfoAlert(module)
        return UnknownTopicAlert(topic)
    }
}