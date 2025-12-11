import NativeFaces from './NativeVisionCameraMlkitFaces';

export async function detectFaces(base64Image: string) {
  return await NativeFaces.detectFaces(base64Image);
}

export default {
  detectFaces,
};
