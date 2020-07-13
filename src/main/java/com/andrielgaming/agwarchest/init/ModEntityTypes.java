package com.andrielgaming.agwarchest.init;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import com.andrielgaming.agwarchest.entities.mobs.MoltenCreeperEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")//TNTBlock
@Mod.EventBusSubscriber(modid=WarchestMaster.MOD_ID, bus=Bus.MOD)
public final class ModEntityTypes 
{
	//Initialize DeferredRegister for entities to be called by the init classes
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, WarchestMaster.MOD_ID);
	public static EntityType<MoltenCreeperEntity> MCREEPER = null;
		
	public static final RegistryObject<EntityType<PrimedC4>> C4_DUMMY_ENTITY = ENTITY_TYPES
			.register("c4_dummy_entity", () -> EntityType.Builder.<PrimedC4>
				create(PrimedC4::new, EntityClassification.MISC).size(0.98f, 0.98f)
							.build(new ResourceLocation(WarchestMaster.MOD_ID, "c4_dummy_entity").toString()));
	
	public static final RegistryObject<EntityType<MoltenCreeperEntity>> MOLTEN_CREEPER = ENTITY_TYPES
			.register("molten_creeper", () -> EntityType.Builder.<MoltenCreeperEntity>
				create(MoltenCreeperEntity::new, EntityClassification.MONSTER).immuneToFire().size(EntityType.CREEPER.getWidth(),EntityType.CREEPER.getHeight())
							.build(new ResourceLocation(WarchestMaster.MOD_ID, "molten_creeper").toString()));

	//put(EntityType.CREEPER, CreeperEntity.func_234278_m_().func_233813_a_())
	public static final void registerMobAttributes()
	{
		GlobalEntityTypeAttributes.put(ModEntityTypes.MOLTEN_CREEPER.get(), MoltenCreeperEntity.func_234278_m_().func_233813_a_());
	}
	
	public static final void registerEntityWorldSpawn(EntityType<?> entity, Biome... biomes) 
	{
        for (Biome biome : biomes) 
        {
        	biome.getSpawns(entity.getClassification()).add(new SpawnListEntry(entity, 35, 1, 1));
        }
    }
	
    public static final void registerEntityWorldSpawns() 
    {
        registerEntityWorldSpawn(ModEntityTypes.MOLTEN_CREEPER.get(), Biomes.field_235254_j_, Biomes.field_235252_ay_, Biomes.field_235253_az_, Biomes.field_235250_aA_, Biomes.field_235251_aB_);
    }
    
    public static final void registerEntitySpawnPlacements()
    {
    	EntitySpawnPlacementRegistry.register(MOLTEN_CREEPER.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawnInLight);
    }
}


