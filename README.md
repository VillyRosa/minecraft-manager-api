# Minecraft Manager

A platform for creating and managing Minecraft servers in a simple, organized, and independent way.

Minecraft Manager turns each world into its own server, with an isolated identity, configuration, and lifecycle. This makes it possible to keep multiple worlds available in the same environment without losing track of where each one is running or how it is configured.

## The idea

Managing Minecraft servers often means dealing with scattered files, ports, manual configuration, and processes that are difficult to keep track of. This project brings those operations together in one place, treating each world as a manageable unit.

Each world can be created from scratch or brought in from an existing adventure. From there, the system handles the storage it needs and a dedicated server to run it.

## What it enables

- Create worlds with their own game rules and characteristics.
- Import existing worlds while preserving their essential information.
- Export worlds to back up, share, or move adventures.
- Run each server in an isolated Docker container.
- Control each server's lifecycle: start, stop, and restart.
- Track the address, port, and status of every world.
- Adjust settings such as the server message, memory, player limit, and game distances.
