import { Text, View, StyleSheet } from 'react-native';
import { detectFaces } from 'react-native-vision-camera-mlkit-faces';

const base64String = '';
const result = detectFaces(base64String);

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
