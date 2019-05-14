
float fmap(float x) {
	return x - int(x);
}

void main (void)  {
	int tileId = int(texture2D(uTexture[0], vTexCoord0).r*255.0+0.5);
	vec2 tileCoords = vec2(fmap(vPosition.x), fmap(vPosition.y));
//	if (abs(tileCoords.x) < 0.1 || abs(tileCoords.y) < 0.1)
//		discard;
	if (tileId < 1) {
		gl_FragColor = red;
	} else if (tileId > 2) {
		gl_FragColor = green;
	} else {
		if (tileId == 1) {
			gl_FragColor = texture2D(uTexture[1], tileCoords);
//			gl_FragColor = vec4(tileCoords, 0.0,1.0);
		} else if (tileId==2) {
			gl_FragColor = texture2D(uTexture[2], tileCoords);
//			gl_FragColor = blue;
//			if (length(tileCoords - vec2(0.5,0.5)) < 0.2)
//				gl_FragColor = green;
		} else
			discard;
	//	gl_FragColor = vec4(vTexCoord0, float(tileId/2.0), 1.0);
	}
}

