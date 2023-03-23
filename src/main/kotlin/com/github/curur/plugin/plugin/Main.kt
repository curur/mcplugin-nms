package com.github.curur.plugin.plugin

import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onEnable() {
        logger.info("enable")
    }

}