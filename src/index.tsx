import NativeFaces from './NativeVisionCameraMlkitFaces';

export async function detectFaces(base64Image: string) {
  return await NativeFaces.detectFaces(base64Image);
}

// Default export (optional)
export default {
  detectFaces,
};
