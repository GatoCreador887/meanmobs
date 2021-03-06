package gatogamer887.meanmobs.init.entity;

import java.util.Random;

import gatogamer887.meanmobs.MeanMobs;
import gatogamer887.meanmobs.init.MeanMobsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EntityUtils {
	
	public static boolean applyGear(EntityLiving entity, String gearFormat) {
		
		String[] strs = gearFormat.split(",");
		
		try {
			
			if (entity.getRNG().nextInt(Integer.parseInt(strs[1])) == 0) {
				
				if (!strs[0].equals("default")) {
					
					entity.setCustomNameTag(strs[0]);
					
				}
				
				entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getByNameOrId(strs[2])));
				entity.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Item.getByNameOrId(strs[3])));
				entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Item.getByNameOrId(strs[4])));
				entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Item.getByNameOrId(strs[5])));
				entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Item.getByNameOrId(strs[6])));
				entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Item.getByNameOrId(strs[7])));
				
				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(getSpeedMod(entity.getRNG(), Double.parseDouble(strs[8])));
				entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(getFollowRangeMod(entity.getRNG(), Double.parseDouble(strs[9])));
				entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(getHealthMod(entity.getRNG(), Double.parseDouble(strs[10])));
				
				return true;
				
			}
			
		} catch (NumberFormatException nfe) {
			
			MeanMobs.logger.warn("The gear format " + gearFormat + " is invalid");
			
		}
		
		return false;
		
	}
	
	public static AttributeModifier getSpeedMod(Random rand, double modifier, double base) {
		
		return new AttributeModifier("speedMod", base + rand.nextDouble() * MeanMobsConfig.mobBuffs.maxSpeedBoost * modifier, 0);
		
	}
	
	public static AttributeModifier getSpeedMod(Random rand, double modifier) {
		
		return getSpeedMod(rand, modifier, 0.0D);
		
	}
	
	public static AttributeModifier getSpeedMod(Random rand) {
		
		return getSpeedMod(rand, 1.0D);
		
	}
	
	public static AttributeModifier getFollowRangeMod(Random rand, double modifier, double base) {
		
		return new AttributeModifier("followRangeMod", base + rand.nextDouble() * MeanMobsConfig.mobBuffs.maxFollowRangeBoost * modifier, 0);
		
	}
	
	public static AttributeModifier getFollowRangeMod(Random rand, double modifier) {
		
		return getFollowRangeMod(rand, modifier, 0.0D);
		
	}
	
	public static AttributeModifier getFollowRangeMod(Random rand) {
		
		return getFollowRangeMod(rand, 1.0D);
		
	}
	
	public static AttributeModifier getHealthMod(Random rand, double modifier, double base) {
		
		return new AttributeModifier("healthMod", base + rand.nextDouble() * MeanMobsConfig.mobBuffs.maxHealthBoost * modifier, 0);
		
	}
	
	public static AttributeModifier getHealthMod(Random rand, double modifier) {
		
		return getHealthMod(rand, modifier, 0.0D);
		
	}
	
	public static AttributeModifier getHealthMod(Random rand) {
		
		return getHealthMod(rand, 1.0D);
		
	}
	
	public static void summonMob(EntityLiving summoner, int chance, String[] helpers, LivingEvent event) {
		
		EntityLiving newMob = null;
		boolean otherSummoned = false;
		
		for (String str : helpers) {
			
			String[] strs = str.split(",");
			
			String id = strs[0];
			double chance1 = Double.parseDouble(strs[1]);
			
			if (summoner.getRNG().nextDouble() < chance1) {
				
				newMob = (EntityLiving) EntityList.createEntityByIDFromName(new ResourceLocation(id), summoner.world);
				otherSummoned = true;
				break;
				
			}
			
		}
		
		if (!otherSummoned) {
			
			newMob = (EntityLiving) EntityList.createEntityByIDFromName(EntityList.getKey(summoner), summoner.world);
			
		}
		
		EntityLivingBase entitylivingbase = ((EntityLiving) summoner).getAttackTarget();
		EntityLivingBase entitylivingbase1 = ((EntityLiving) summoner).getRevengeTarget();
		Vec3d location = new Vec3d(summoner.posX, summoner.posY + summoner.getEyeHeight(), summoner.posZ);
		Vec3d newMobLocation;
		Vec3d direction;
		
		if (event instanceof LivingHurtEvent) {
			
			DamageSource source = ((LivingHurtEvent) event).getSource();

	        if (entitylivingbase == null && source.getTrueSource() instanceof EntityLivingBase)
	        {
	            entitylivingbase = (EntityLivingBase)source.getTrueSource();
	        }
			
		}
        
        int i = MathHelper.floor(summoner.posX);
        int j = MathHelper.floor(summoner.posY);
        int k = MathHelper.floor(summoner.posZ);
		
		if (summoner.getRNG().nextInt(chance) == 0) {
			
			for (int l = 0; l < 50; ++l)
	        {
	            int i1 = i + MathHelper.getInt(summoner.getRNG(), 7, 40) * MathHelper.getInt(summoner.getRNG(), -1, 1);
	            int j1 = j + MathHelper.getInt(summoner.getRNG(), 7, 40) * MathHelper.getInt(summoner.getRNG(), -1, 1);
	            int k1 = k + MathHelper.getInt(summoner.getRNG(), 7, 40) * MathHelper.getInt(summoner.getRNG(), -1, 1);

	            if (summoner.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isSideSolid(summoner.world, new BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP) && summoner.world.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10)
	            {
	                newMob.setPosition((double)i1, (double)j1, (double)k1);
	                newMobLocation = new Vec3d(newMob.posX, newMob.posY + newMob.getEyeHeight(), newMob.posZ);
	                direction = location.subtract(newMobLocation).normalize();
	                //Invert (turn to negative) direction
	                direction = direction.subtract(direction.add(direction));
	                net.minecraftforge.fml.common.eventhandler.Event.Result canSpawn = net.minecraftforge.event.ForgeEventFactory.canEntitySpawn(newMob, summoner.world, (float)newMob.posX, (float)newMob.posY, (float)newMob.posZ, null);

	                if (!summoner.world.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 7.0D) && summoner.world.checkNoEntityCollision(newMob.getEntityBoundingBox(), newMob) && summoner.world.getCollisionBoxes(newMob, newMob.getEntityBoundingBox()).isEmpty() && !summoner.world.containsAnyLiquid(newMob.getEntityBoundingBox()) && (canSpawn == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW || canSpawn == net.minecraftforge.fml.common.eventhandler.Event.Result.DEFAULT))
	                {
	                    summoner.world.spawnEntity(newMob);
	                    if (MeanMobsConfig.debugging.logAssistanceSummoning) MeanMobs.logger.debug(EntityList.getKey(summoner) + " summoned an entity of type " + EntityList.getKey(newMob) + " at " + newMobLocation);
	                    if (entitylivingbase != null) newMob.setAttackTarget(entitylivingbase);
	                    if (entitylivingbase1 != null) newMob.setRevengeTarget(entitylivingbase1);
	                    if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(newMob, summoner.world, (float)newMob.posX, (float)newMob.posY, (float)newMob.posZ, null)) newMob.onInitialSpawn(summoner.world.getDifficultyForLocation(new BlockPos(newMob)), (IEntityLivingData)null);

	                    if (MeanMobsConfig.summoning.summoningEffects)
	                    {
	                    	summoner.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 1.0F, 2.0F);
		                    newMob.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, 0.85F);

		                    if (MeanMobs.side == Side.CLIENT && Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().world.isRemote)
		                    {
		                    	double dist = summoner.getDistance(newMobLocation.x, newMobLocation.y, newMobLocation.z);

		                    	for (double e1 = 0; e1 < dist; e1 += 0.2D)
		                    	{
		                    		Vec3d dir = direction.scale(e1);
		                    		Vec3d pos = location.add(dir);
		                    		Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
		                    	}

			                    for (int e = 0; e < 40; ++e)
			                    {
			                        double d2 = newMob.getRNG().nextGaussian() * 0.02D;
			                        double d0 = newMob.getRNG().nextGaussian() * 0.02D;
			                        double d1 = newMob.getRNG().nextGaussian() * 0.02D;
			                        Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, newMob.posX + (double)(newMob.getRNG().nextFloat() * newMob.width * 2.0F) - (double)newMob.width, newMob.posY + (double)(newMob.getRNG().nextFloat() * newMob.height), newMob.posZ + (double)(newMob.getRNG().nextFloat() * newMob.width * 2.0F) - (double)newMob.width, d2, d0, d1);
			                    }
		                    }
	                    }

	                    break;
	                }
	            }
	        }
			
		}
		
	}
	
	public static void applyZombieSpawnBuffs(EntityZombie zombie) {
		
		zombie.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(EntityUtils.getSpeedMod(zombie.getRNG()));
		zombie.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(EntityUtils.getFollowRangeMod(zombie.getRNG(), 1.0D, 15.0D));
		zombie.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(EntityUtils.getHealthMod(zombie.getRNG()));
		
		for (String str : MeanMobsConfig.mobBuffs.zombieGearPresets) {
			
			if (EntityUtils.applyGear(zombie, str)) {
				
				break;
				
			}
			
		}
		
	}
	
	public static void applySkeletonSpawnBuffs(AbstractSkeleton skeleton) {
		
		skeleton.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(EntityUtils.getSpeedMod(skeleton.getRNG()));
		skeleton.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(EntityUtils.getFollowRangeMod(skeleton.getRNG(), 1.0D, 34.0D));
		skeleton.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(EntityUtils.getHealthMod(skeleton.getRNG()));
		
		for (String str : MeanMobsConfig.mobBuffs.skeletonGearPresets) {
			
			if (EntityUtils.applyGear(skeleton, str)) {
				
				break;
				
			}
			
		}
		
	}
	
}
