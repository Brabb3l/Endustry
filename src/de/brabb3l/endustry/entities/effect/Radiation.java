package de.brabb3l.endustry.entities.effect;

import static io.anuke.mindustry.Vars.*;

import java.io.*;

import de.brabb3l.endustry.*;
import io.anuke.arc.collection.*;
import io.anuke.arc.math.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.entities.*;
import io.anuke.mindustry.entities.traits.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.game.*;
import io.anuke.mindustry.world.*;

public class Radiation extends TimedEntity implements SaveTrait, SyncTrait {

	private static final IntMap<Radiation> map = new IntMap<>();
	private static final float baseLifetime = 3000f;
	
    public static EntityGroup<Radiation> radiantsGroup;

	private int loadedPosition = -1;
	private Tile tile;
	private Block block;
	private float lifetime;
	private float intensity;

	/** Deserialization use only! */
	public Radiation() {
		super();
	}

	public static void onRemoveRadiation(int fid) {
		radiantsGroup.removeByID(fid);
	}

	/** Start a fire on the tile. If there already is a file there, refreshes its lifetime. */
	public static void create(Tile tile) {
		if (net.client() || tile == null)
			return; // not clientside.

		Radiation rad = map.get(tile.pos());

		if (rad == null) {
			rad = new Radiation();
			rad.tile = tile;
			rad.lifetime = baseLifetime;
			rad.set(tile.worldx(), tile.worldy());
			rad.add();
			
			map.put(tile.pos(), rad);
		} else {
			rad.intensity++;
			rad.lifetime += baseLifetime;
			rad.time = 0f;
		}
	}

	public static boolean has(int x, int y) {
		if (!Structs.inBounds(x, y, world.width(), world.height()) || !map.containsKey(Pos.get(x, y)))
			return false;

		Radiation rad = map.get(Pos.get(x, y));
		return rad.isAdded() && rad.fin() < 1f && rad.tile != null && rad.tile.x == x && rad.tile.y == y;
	}

	@Override
	public TypeID getTypeID() {
		return ModTypeIDs.radiation;
	}

	@Override
	public byte version() {
		return 0;
	}

	@Override
	public float lifetime() {
		return lifetime;
	}

	@Override
	public void update() {
		if (Mathf.chance(0.05 * Time.delta())) {
			Effects.effect(ModFx.radiation, x + Mathf.range(4f), y + Mathf.range(4f));
		}

		if (Mathf.chance(0.02 * Time.delta())) {
			Effects.effect(ModFx.radiation, x + Mathf.range(4f), y + Mathf.range(4f));
		}

		if (Mathf.chance(0.001 * Time.delta())) {
			if (ModSounds.geiger != null)
				ModSounds.geiger.at(this);
		}

		time = Mathf.clamp(time + Time.delta(), 0, lifetime());
		map.put(tile.pos(), this);

		if (net.client())
			return;

		if (time >= lifetime() || tile == null) {
			remove();
			return;
		}

		TileEntity entity = tile.link().entity;
		boolean damage = entity != null;
		
		if (damage) {
			lifetime += Time.delta() * 0.25f;
		}

		if (Mathf.chance(0.1 * Time.delta())) {
			if (damage)
				entity.damage(0.4f * intensity);
			
			Damage.damageUnits(null, tile.worldx(), tile.worldy(), tilesize, 3f * intensity, unit -> true, unit -> unit.applyEffect(ModStatusEffects.radiated, 60 * 15));
		}
	}

	@Override
	public void writeSave(DataOutput stream) throws IOException {
		stream.writeInt(tile.pos());
		stream.writeFloat(lifetime);
		stream.writeFloat(time);
		stream.writeFloat(intensity);
	}

	@Override
	public void readSave(DataInput stream, byte version) throws IOException {
		this.loadedPosition = stream.readInt();
		this.lifetime = stream.readFloat();
		this.time = stream.readFloat();
		this.intensity = stream.readFloat();
		add();
	}

	@Override
	public void write(DataOutput data) throws IOException {
		data.writeInt(tile.pos());
		data.writeFloat(lifetime);
		data.writeFloat(intensity);
	}

	@Override
	public void read(DataInput data) throws IOException {
		int pos = data.readInt();
		this.lifetime = data.readFloat();
		this.intensity = data.readFloat();

		x = Pos.x(pos) * tilesize;
		y = Pos.y(pos) * tilesize;
		tile = world.tile(pos);
	}

	@Override
	public void reset() {
		loadedPosition = -1;
		tile = null;
		incrementID();
	}

	@Override
	public void added() {
		if (loadedPosition != -1) {
			map.put(loadedPosition, this);
			tile = world.tile(loadedPosition);
			set(tile.worldx(), tile.worldy());
		}
	}

	@Override
	public void removed() {
		if (tile != null) {
			ModCall.onRemoveRadiation(id);
			map.remove(tile.pos());
		}
	}

	@Override
	public EntityGroup targetGroup() {
		return radiantsGroup;
	}
}
