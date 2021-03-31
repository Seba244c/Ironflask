package dk.sebsa.ironflask.engine.terra;

import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.components.EntityRenderer;
import dk.sebsa.ironflask.engine.graph.Shader;
import dk.sebsa.ironflask.engine.graph.Texture;

public class Terrain {

    private final Entity[] entities;

    public Terrain(int blocksPerRow, float scale, float minY, float maxY, Texture heightMap, Texture texture, int textInc, Entity parent) throws Exception {
    	entities = new Entity[blocksPerRow * blocksPerRow];
        HeightMapMesh heightMapMesh = new HeightMapMesh(minY, maxY, heightMap, texture, textInc);
        for (int row = 0; row < blocksPerRow; row++) {
            for (int col = 0; col < blocksPerRow; col++) {
                float xDisplacement = (col - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) blocksPerRow - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

                Entity terrainBlock = new Entity(false);
                terrainBlock.parent(parent);
                terrainBlock.addComponent(new EntityRenderer(heightMapMesh.getMesh(), heightMapMesh.getMaterial(), Shader.getShader("default")));
                terrainBlock.setLocalScale(scale);
                terrainBlock.setLocalPosition(new Vector3f(xDisplacement, 0, zDisplacement));
                entities[row * blocksPerRow + col] = terrainBlock;
            }
        }
    }

    public Entity[] getEntities() {
        return entities;
    }
}
