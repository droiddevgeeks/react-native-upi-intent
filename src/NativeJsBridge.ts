import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  addJsBridge(viewTag: number): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('JsBridge');
