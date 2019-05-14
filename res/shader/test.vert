
void main(void) {
	vec4 vertex = uModelMatrix * gl_Vertex;
	vNormal = mat3transformNormal(gl_Normal.xyz, uNormalMatrix);
	vTexCoord0 = gl_MultiTexCoord0.xy;
	vPosition = gl_Vertex.xyz;
	vertex = (uProjectionMatrix * (uViewMatrix * vertex));
	gl_Position = vertex;
}

