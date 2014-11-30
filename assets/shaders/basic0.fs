precision mediump float;
uniform vec4 vColor;

void main() {
	gl_FragColor = vColor * 0.0001 + vec4(0.8, 0.1, 0.1, 1.0);
}