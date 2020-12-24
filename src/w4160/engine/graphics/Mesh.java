package w4160.engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.system.MemoryUtil;

/**
 * A class containing the structure of an object, as well as texture and material.
 */
public class Mesh {

    private int vaoId;

    private List<Integer> vboIdList;

    private int vertexCount;

    private Material material;
    
    private float[] pos;
    private float[] textco;
    private float[] norms;
    private int[] inds;
    
    /**
     * Default constructor. Create a mesh representing a cube.
     */
    public Mesh(){
    	// here we hardcode the mesh data for a cube. 
    	this(new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,1.0f,0.0f,0.0f,1.0f,1.0f,1.0f,0.0f,0.0f,1.0f,0.0f,1.0f,1.0f,1.0f,0.0f,1.0f,1.0f,1.0f}, 
    		 new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    		 new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
    		 new int[]{0,6,4,0,2,6,0,3,2,0,1,3,2,7,6,2,3,7,4,6,7,4,7,5,0,4,5,0,5,1,1,5,7,1,7,3});
    }

    /**
     * Binds the positions, text coordinates, normals, and indices to OpenGL buffers,
     * assuming that all faces are triangles.
     * 
     * The method will be called when use OpenGL to render this mesh.
     *
     * @param positions An array of the individual positions (x,y,z) of all the vertices
     * @param textCoords An array of the individual texture coordinates (tx, ty) of all the vertices
     * @param normals An array of the individual normal coordinates (nx, ny, nz) of all the vertices
     * @param indices An array of the indices of vertices. Each group of 3 indices corresponds to a face.
     */
    public void setMesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	
    	pos = positions;
    	textco = textCoords;
    	norms = normals;
    	inds = indices;
    	
    	FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        //System.out.println("create mesh:");
        //System.out.println("v: "+positions.length+" t: "+textCoords.length+" n: "+normals.length+" idx: "+indices.length);
        
        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<Integer>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    /**
     * Construct a triangle mesh with a provided position, normal, and texture data.
     * 
     * <p>
     * <b>Note:</b> The provided indices are used to index data in all three arrays (namely, position, textCoords,
     * and normals). You need to make sure position, textCoords, and normals are properly ordered so that a 
     * a single array of indices can be used for indexing.
     * </p>
     * 
     * @param positions An array of the individual positions (x,y,z) of all the vertices
     * @param textCoords An array of the individual texture coordinates (tx, ty) of all the vertices
     * @param normals An array of the individual normal coordinates (nx, ny, nz) of all the vertices
     * @param indices An array of the indices of vertices. Each group of 3 indices corresponds to a face.
     */
    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	setMesh(positions, textCoords, normals, indices);        
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Display the mesh on the screen by calling OpenGL routines.
     */
    public void render() {
    	// Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    /**
     * Scale the current mesh by the given x, y, z parameters.
     *
     * @param sx The scale along x axis
     * @param sy The scale along y axis
     * @param sz The scale along z axis
     */
    public void scaleMesh(float sx, float sy, float sz){
    	cleanUp(); //clean up buffer
    	//reset position of each point
    	for (int i = 0; i < pos.length/3; i++) {
			pos[3*i]   = pos[3*i]*sx;
			pos[3*i+1] = pos[3*i+1]*sy;
			pos[3*i+2] = pos[3*i+2]*sz;
		}
    	
    	setMesh(pos, textco, norms, inds);
    }

    /**
     * Translate the current mesh along a given vector.
     *
     * @param trans The vector along which to translate
     */
    public void translateMesh(Vector3f trans) {
    	cleanUp();
    	//reset position of each point
    	for(int i=0; i< pos.length/3; i++){
    		pos[3*i]   = pos[3*i]   + trans.x;
    		pos[3*i+1] = pos[3*i+1] + trans.y;
    		pos[3*i+2] = pos[3*i+2] + trans.z;
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    /**
     * Rotate the current mesh counterclockwise around a given rotation axis.
     *
     * @param axis The axis along which the rotation is performed
     * @param angle Rotated angle in degree
     * @see <a href="http://mathworld.wolfram.com/RotationFormula.html">Rotation</a>
     */
    public void rotateMesh(Vector3f axis, float angle) {
    	cleanUp();
    	Vector3fc nml = axis.normalize();
    	AxisAngle4f rot = new AxisAngle4f((float)Math.toRadians(angle), nml);
    	//reset position of each point
    	for(int i=0; i< pos.length/3; i++) {
    		Vector3f vtx = new Vector3f(pos[3*i], pos[3*i+1], pos[3*i+2]);
    		rot.transform(vtx);
    		pos[3*i]   = vtx.x;
    		pos[3*i+1] = vtx.y;
    		pos[3*i+2] = vtx.z;
    	}
    	setMesh(pos, textco, norms, inds);
    }
    
    /**
     * Reflect the current mesh with respect to a plane defined by the parameters.
     * 
     * The rotation plane is defined by a point p on the plane and the normal n of the plane.
     * 
     * @param p the point on the plane
     * @param n the normal of the plane
     * @see <a href="http://mathworld.wolfram.com/Reflection.html">Reflection</a>
     */
    public void reflectMesh(Vector3f p, Vector3f n) {
    	cleanUp();
    	Vector3fc nml = n.normalize();
    	//reset position of each point
    	for(int i=0; i< pos.length/3; i++) {
    		Vector3f vtx = new Vector3f(pos[3*i], pos[3*i+1], pos[3*i+2]);
    		vtx.sub(p);	// vtx - p
    		
    		float dist = vtx.dot(nml)*2.f;
    		pos[3*i]   -= dist*nml.x();
    		pos[3*i+1] -= dist*nml.y();
    		pos[3*i+2] -= dist*nml.z();
    	}
    	setMesh(pos, textco, norms, inds);
    }
}
