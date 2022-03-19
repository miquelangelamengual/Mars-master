package me.imluis_.crates.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class BukkitUtil {

	public static final String STRAIGHT_LINE_DEFAULT;
	private static final ImmutableMap<Object, Object> CHAT_DYE_COLOUR_MAP;
	private static final ImmutableSet<Object> DEBUFF_TYPES;
	private static final int DEFAULT_COMPLETION_LIMIT = 80;
	private static final String STRAIGHT_LINE_TEMPLATE;

	private static ArrayList<ChatColor> woolColors = new ArrayList<ChatColor>(Arrays.asList(ChatColor.WHITE, ChatColor.GOLD, ChatColor.LIGHT_PURPLE,
			ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, ChatColor.DARK_GRAY,
			ChatColor.GRAY, ChatColor.DARK_AQUA, ChatColor.DARK_PURPLE, ChatColor.BLUE, ChatColor.BLACK,
			ChatColor.DARK_GREEN, ChatColor.RED));
	
	static{
		STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
		STRAIGHT_LINE_DEFAULT = BukkitUtil.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
		CHAT_DYE_COLOUR_MAP = ImmutableMap.builder().put(ChatColor.AQUA, DyeColor.LIGHT_BLUE).put(ChatColor.BLACK, DyeColor.BLACK).put(ChatColor.BLUE, DyeColor.LIGHT_BLUE).put(ChatColor.DARK_AQUA, DyeColor.CYAN).put(ChatColor.DARK_BLUE, DyeColor.BLUE).put(ChatColor.DARK_GRAY, DyeColor.GRAY).put(ChatColor.DARK_GREEN, DyeColor.GREEN).put(ChatColor.DARK_PURPLE, DyeColor.PURPLE).put(ChatColor.DARK_RED, DyeColor.RED).put(ChatColor.GOLD, DyeColor.ORANGE).put(ChatColor.GRAY, DyeColor.SILVER).put(ChatColor.GREEN, DyeColor.LIME).put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).put(ChatColor.RED, DyeColor.RED).put(ChatColor.WHITE, DyeColor.WHITE).put(ChatColor.YELLOW, DyeColor.YELLOW).build();
		DEBUFF_TYPES = ImmutableSet.builder().add(PotionEffectType.BLINDNESS).add(PotionEffectType.CONFUSION).add(PotionEffectType.HARM).add(PotionEffectType.HUNGER).add(PotionEffectType.POISON).add(PotionEffectType.SATURATION).add(PotionEffectType.SLOW).add(PotionEffectType.SLOW_DIGGING).add(PotionEffectType.WEAKNESS).add(PotionEffectType.WITHER).build();
	}

	public static ArrayList<Location> getCircle(final Location center, final double radius, final int amount) {
        final World world = center.getWorld();
        final double increment = 6.283185307179586 / amount;
        final ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < amount; ++i) {
            final double angle = i * increment;
            final double x = center.getX() + radius * Math.cos(angle);
            final double z = center.getZ() + radius * Math.sin(angle);
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
	
	public static int countColoursUsed(final String id, final boolean ignoreDuplicates){
		final ChatColor[] values = ChatColor.values();
		final List<Character> charList = new ArrayList<Character>(values.length);
		for(final ChatColor colour : values){
			charList.add(colour.getChar());
		}
		int count = 0;
		final Set<ChatColor> found = new HashSet<ChatColor>();
		for(int i = 1; i < id.length(); ++i){
			if(charList.contains(id.charAt(i)) && id.charAt(i - 1) == '&'){
				final ChatColor colour = ChatColor.getByChar(id.charAt(i));
				if(found.add(colour) || ignoreDuplicates){
					++count;
				}
			}
		}
		return count;
	}
	
	public static List<Player> getOnlinePlayers() {
		
		List<Player> players = Lists.newArrayList();
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			players.add(player);
		}
		
		return players;
	}

	public static List<String> getCompletions(final String[] args, final List<String> input){
		return getCompletions(args, input, 80);
	}

	public static List<String> getCompletions(final String[] args, final List<String> input, final int limit){
		Preconditions.checkNotNull(args);
		Preconditions.checkArgument(args.length != 0);
		final String argument = args[args.length - 1];
		return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
	}

	public static String getDisplayName(final CommandSender sender){
		Preconditions.checkNotNull(sender);
		return (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();
	}

	public static DyeColor toDyeColor(final ChatColor colour){
		return (DyeColor) BukkitUtil.CHAT_DYE_COLOUR_MAP.get(colour);
	}

	public static boolean hasMetaData(final Metadatable metadatable, final String input, final Plugin plugin){
		return getMetaData(metadatable, input, plugin) != null;
	}

	public static MetadataValue getMetaData(final Metadatable metadatable, final String input, final Plugin plugin){
		return (MetadataValue) metadatable.getMetadata(input);
	}

	public static Player getFinalAttacker(final EntityDamageEvent ede, final boolean ignoreSelf){
		Player attacker = null;
		if(ede instanceof EntityDamageByEntityEvent){
			final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) ede;
			final Entity damager = event.getDamager();
			if(event.getDamager() instanceof Player){
				attacker = (Player) damager;
			}else if(event.getDamager() instanceof Projectile){
				final Projectile projectile = (Projectile) damager;
				final ProjectileSource shooter = projectile.getShooter();
				if(shooter instanceof Player){
					attacker = (Player) shooter;
				}
			}
			if(attacker != null && ignoreSelf && event.getEntity().equals(attacker)){
				attacker = null;
			}
		}
		return attacker;
	}

	public static Player playerWithNameOrUUID(final String string){
		if(string == null){
			return null;
		}
		return JavaUtil.isUUID(string) ? Bukkit.getPlayer(UUID.fromString(string)) : Bukkit.getPlayer(string);
	}

	@Deprecated
	public static OfflinePlayer offlinePlayerWithNameOrUUID(final String string){
		if(string == null){
			return null;
		}
		return JavaUtil.isUUID(string) ? Bukkit.getOfflinePlayer(UUID.fromString(string)) : Bukkit.getOfflinePlayer(string);
	}

	public static boolean isWithinX(final Location location, final Location other, final double distance){
		return location.getWorld().equals(other.getWorld()) && Math.abs(other.getX() - location.getX()) <= distance && Math.abs(other.getZ() - location.getZ()) <= distance;
	}

	public static Location getHighestLocation(final Location origin){
		return getHighestLocation(origin, null);
	}

	public static Location getHighestLocation(final Location origin, final Location def){
		Preconditions.checkNotNull(origin, "The location cannot be null");
		final Location cloned = origin.clone();
		final World world = cloned.getWorld();
		final int x = cloned.getBlockX();
		int y = world.getMaxHeight();
		final int z = cloned.getBlockZ();
		while(y > origin.getBlockY()){
			final Block block = world.getBlockAt(x, --y, z);
			if(!block.isEmpty()){
				final Location next = block.getLocation();
				next.setPitch(origin.getPitch());
				next.setYaw(origin.getYaw());
				return next;
			}
		}
		return def;
	}

	public static boolean isDebuff(final PotionEffectType type){
		return BukkitUtil.DEBUFF_TYPES.contains(type);
	}

	public static boolean isDebuff(final PotionEffect potionEffect){
		return isDebuff(potionEffect.getType());
	}

	public static boolean isDebuff(final ThrownPotion thrownPotion){
		for(final PotionEffect effect : thrownPotion.getEffects()){
			if(isDebuff(effect)){
				return true;
			}
		}
		return false;
	}

	public static int getDefaultCompletionLimit() {
		return DEFAULT_COMPLETION_LIMIT;
	}
	
	public static String getName(PotionEffectType potionEffectType) {
		if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
			return "Fire Resistance";
		} else if (potionEffectType.getName().equalsIgnoreCase("speed")) {
			return "Speed";
		} else if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
			return "Weakness";
		} else if (potionEffectType.getName().equalsIgnoreCase("slowness")) {
			return "Slowness";
		} else {
			return "Unknown";
		}
	}

	public static Player getDamager(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			return (Player) event.getDamager();
		} else if (event.getDamager() instanceof Projectile) {
			if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
				return (Player) ((Projectile) event.getDamager()).getShooter();
			}
		}

		return null;
	}

	public static int convertChatColorToWoolData(ChatColor color) {
		if(color == ChatColor.DARK_RED) {
			color = ChatColor.RED;
		}
		
		return woolColors.indexOf(color);
	}

}
