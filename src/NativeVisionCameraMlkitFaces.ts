import { TurboModuleRegistry } from 'react-native';

export interface Spec {
  detectFaces(base64Image: string): Promise<any[]>;
}

const module = TurboModuleRegistry.getEnforcing(
  'VisionCameraMlkitFaces'
) as Spec;

export default module;
