package w4160.engine.graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * 	This class implements the method that loads an .obj file and returns a {@link Mesh}
 *  instance.
 *  
 */
public class OBJLoader {
	
	// Disable the constructor. Only the static method is exposed.
	private OBJLoader() {}
	
	/**
	 * The method reads an input .OBJ file and returns a {@link Mesh} instance.
	 * The input .OBJ file may contains vertex positions, texture coordinates, and vertex normals.<p>
	 * 
	 *  <b>Note:</b> In the given .OBJ file, the number of vertices, the number of texture coordinates (if provided),
	 *  and the number of vertex normals (if provided) must be the same. 
	 * 
	 * @param fileName file name of the .OBJ file.
	 * @return a {@link Mesh} instance representing the loaded mesh.
	 * @throws IOException if the given input file cannot be read successfully.
	 * @see Mesh
	 */
    public static Mesh loadMesh(String fileName) throws IOException {
    	//student code
    	
    	System.out.println("OBJLoader: loading "+fileName);
        // List<String> lines = Utils.readAllLines(fileName);
    	
    	List<Vector3fc> vertices = new ArrayList<>();
        List<Vector2fc> textures = new ArrayList<>();
        List<Vector3fc> normals  = new ArrayList<>();
        List<Face> faces        = new ArrayList<>();
    	
    	BufferedReader in = new BufferedReader(new FileReader(fileName));
    	String str;
    	
    	while((str = in.readLine()) != null) {
    		String[] tokens = str.split("\\s+");
    		
    		switch (tokens[0]) {
            	case "v":
	                // Geometric vertex
	                vertices.add( new Vector3f(
	                        Float.parseFloat(tokens[1]),
	                        Float.parseFloat(tokens[2]),
	                        Float.parseFloat(tokens[3])) );
	                break;
	            case "vt":
	                // Texture coordinate
	            	textures.add( new Vector2f(
	                        Float.parseFloat(tokens[1]),
	                        Float.parseFloat(tokens[2])) );
	                break;
	            case "vn":
	                // Vertex normal
	                normals.add( new Vector3f(
	                        Float.parseFloat(tokens[1]),
	                        Float.parseFloat(tokens[2]),
	                        Float.parseFloat(tokens[3])) );
	                break;
	            case "f":
	                faces.add( new Face(tokens[1], tokens[2], tokens[3]) );
	                break;
	            default:
	                // Ignore other lines
	                break;
    		}
        }
    	in.close();

        return createMesh(vertices, textures, normals, faces);
    }

    private static Mesh createMesh(
    		List<Vector3fc> posList, 
    		List<Vector2fc> textCoordList,
            List<Vector3fc> normList, 
            List<Face> facesList) {

    	HashMap<IdxGroup, Integer> vtxMap = new HashMap<>();
    	int cnt = 0;
    	for (Face face : facesList) {
    		IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
    		for (IdxGroup indValue : faceVertexIndices) {
    			if ( !vtxMap.containsKey(indValue) ) {
    				vtxMap.put(indValue, cnt++);
    			}
    		} // end for
    	}
    	System.out.println(cnt + " vertices will be created in memory.");
    	
    	float[] posArr = new float[cnt * 3];
    	float[] textCoordArr = new float[cnt * 2];
    	float[] normArr = new float[cnt * 3];
    	int[] indicesArr = new int[facesList.size()*3];
    	
    	// initialize the array with NaN for sanity check later
        for(int j = 0;j < posArr.length;++ j) posArr[j] = Float.NaN;
    	for(int j = 0;j < textCoordArr.length;++ j) textCoordArr[j] = 0f;
        for(int j = 0;j < normArr.length;++ j) normArr[j] = 0f;
        
    	cnt = 0;
    	// now fill the buffer
    	for (Face face : facesList) {
    		IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
    		for (IdxGroup indValue : faceVertexIndices) {
    			indicesArr[cnt] = vtxMap.get(indValue);
    			
    			int tt = indicesArr[cnt] * 3;
    			if ( Float.isNaN(posArr[tt]) )	{
    				// this is the first time this vertex appears
    				Vector3fc pos = posList.get(indValue.idxPos);
    				posArr[tt]   = pos.x();
    				posArr[tt+1] = pos.y();
    				posArr[tt+2] = pos.z();
    				
    				if ( indValue.idxTextCoord >= 0 ) {
    					// has texture coordinate
    					tt = indicesArr[cnt] * 2;
    					Vector2fc textCoord = textCoordList.get(indValue.idxTextCoord);
    					textCoordArr[tt]   = textCoord.x();
    					// NOTE: Here we flip the Y-coordinate
    					// This is because the image loaded from pixel file starts its (0,0) at top-left corner. 
    					// But when OpenGL loads the pixel data, it treats (0, 0) at bottom-left corner. 
    					textCoordArr[tt+1] = 1f - textCoord.y();
    				}
    				
    				if ( indValue.idxVecNormal >= 0 ) {
    					// has normals
    					tt = indicesArr[cnt] * 3;
    					Vector3fc vecNorm = normList.get(indValue.idxVecNormal);
    					normArr[tt]   = vecNorm.x();
    		            normArr[tt+1] = vecNorm.y();
    		            normArr[tt+2] = vecNorm.z();
    				}
    			}
    			++ cnt;
    		}
    	} // end for face
    	assert cnt == indicesArr.length : "Something wrong!";
    	// finally create the mesh
        return new Mesh(posArr, textCoordArr, normArr, indicesArr);
    }

    private static class Face {

        /**
         * List of idxGroup groups for a face triangle (3 vertices per face).
         */
        private IdxGroup[] idxGroups = new IdxGroup[3];

        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];
            // Parse the lines
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        private IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1; // vertex index
            
            if (length > 1) {
                // It can be empty if the OBJ file does not define text coords
                String textCoord = lineTokens[1];
                idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
                
                if (length > 2) {
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return idxGroup;
        } // end method

        public IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    private static class IdxGroup {

        public static final int NO_VALUE = -1;

        public int idxPos;

        public int idxTextCoord;

        public int idxVecNormal;

        public IdxGroup() {
            idxPos       = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + idxPos;
            result = prime * result + idxTextCoord;
            result = prime * result + idxVecNormal;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
        	if (this == obj) {
                return true;
            }
        	if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
        	
        	IdxGroup other = (IdxGroup) obj;
        	
            return (idxPos == other.idxPos &&
            		idxTextCoord == other.idxTextCoord &&
            		idxVecNormal == other.idxVecNormal);
        }
    }
}
