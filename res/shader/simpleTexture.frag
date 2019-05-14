void main (void)  {
	vec4 color = texture2D(uTexture[0], vTexCoord0);
	gl_FragColor = color;
}

